package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.common.service.CommentAndReplyService
import com.it.onefool.nsfw18.domain.dto.*
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.domain.vo.CommentReplyVo
import com.it.onefool.nsfw18.domain.vo.CommentVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CommentMapper
import com.it.onefool.nsfw18.service.CommentReplyService
import com.it.onefool.nsfw18.service.CommentService
import com.it.onefool.nsfw18.utils.BeanUtils
import com.it.onefool.nsfw18.utils.JwtUtil
import com.it.onefool.nsfw18.utils.LocalDateUtils
import jakarta.servlet.http.HttpServletRequest
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.servlet.ServletRequest

/**
 * @author 97436
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:19
 */
@Service
class CommentServiceImpl : ServiceImpl<CommentMapper?, Comment?>(), CommentService {
    companion object {
        private val logger = LoggerFactory.getLogger(CommentServiceImpl::class.java)
    }

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var beanUtils: BeanUtils

    @Autowired
    private lateinit var commentReplyService: CommentReplyService

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    private lateinit var localDateUtils: LocalDateUtils

    @Autowired
    private lateinit var commentAndReplyService: CommentAndReplyService

    /**
     * 从漫画查询中去分页查询评论列表(数据库直查)
     */
    override fun pageComment(pageRequestDto: PageRequestDto<Comment?>)
            : Result<PageInfo<CommentVo>> {
        val commentList = mutableListOf<CommentVo>()
        val pageSize = pageRequestDto.size
        val pageSum = pageRequestDto.page
        val comment = pageRequestDto.body ?: throw CustomizeException(
            StatusCode.NOT_FOUND_COMMENT.code(), StatusCode.NOT_FOUND_COMMENT.message()
        )
        val page = Page<Comment>(pageSum, pageSize)
        val qwComment = QueryWrapper<Comment>()
        qwComment
            .eq("cartoon_id", comment.cartoonId)
            .orderByAsc("create_time")
        this.baseMapper?.selectPage(page, qwComment)
        beanUtils.copyCommentAndCommentVo(page.records, commentList)
        return Result.ok(
            PageInfo<CommentVo>(
                page.current,
                page.size,
                page.total,
                page.pages,
                commentList
            )
        )
    }

    /**
     * 直接的分页查询评论列表
     */
    override fun pageComment(
        pageRequestDto: PageRequestDto<CommentConDto?>,
        request: HttpServletRequest
    ): Result<PageInfo<CommentVo>> {
        pageRequestDto.body?.let outer@{ c ->
            c.cartoonId?.let {
                //为空说明用户没有登录，有值说明用户登录了
                val userDto = jwtUtil.getToken(request)
                return getComment(pageRequestDto, userDto)
            }
        }
        throw CustomizeException(
            StatusCode.NOT_FOUND_COMMENT.code(),
            StatusCode.NOT_FOUND_COMMENT.message()
        )
    }

    /**
     * 漫画章节分页查询评论列表(从controller来的)
     */
    override fun pageComment(
        currentPage: Long,
        pageSize: Long,
        cartoonId: Int,
        chapterId: Int
    ): Result<PageInfo<CommentVo>> {
        val comment = CommentConDto().apply {
            this.cartoonId = cartoonId
            this.chapterId = chapterId
        }
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        val servlet = requestAttributes as ServletRequestAttributes
        val request = servlet.request
        return pageComment(PageRequestDto(currentPage, pageSize, comment), request)
    }

    /**
     * 删除一级评论
     */
    override fun deleteComment(id: Int): Result<String> {
        val qw = QueryWrapper<Comment>()
        qw.eq("id", id)
        val comment = this.getOne(qw) ?: throw CustomizeException(
            StatusCode.DELETE_COMMENT_FAIL.code(), StatusCode.DELETE_COMMENT_FAIL.message()
        )
        transactionTemplate.execute {
            try {
                val flag = this.remove(qw)
                if (!flag) throw RuntimeException("...")
            } catch (e: Exception) {
                it.setRollbackOnly()
                logger.error("数据库删除一级评论失败,原因{}", e.message)
                throw CustomizeException(
                    StatusCode.DELETE_COMMENT_FAIL.code(), StatusCode.DELETE_COMMENT_FAIL.message()
                )
            }
        }
        redisTemplate.opsForZSet().remove(
            CacheConstants.COMMENTS_REPLY +
                    comment.cartoonId + comment.chapterId,
            comment
        )
        return Result.ok()
    }

    /**
     * 插入一级评论
     */
    override fun addComment(
        comment: CommentConDto,
        request: HttpServletRequest
    ): Result<PageInfo<CommentVo>> {
        val cartoonId = comment.cartoonId
        val content = comment.content
        cartoonId?.let {
            // 评论不能为空
            if (content.isNullOrBlank()) throw CustomizeException(
                StatusCode.COMMENTS_CANNOT_BE_EMPTY.code(),
                StatusCode.COMMENTS_CANNOT_BE_EMPTY.message()
            )
            val userDto = jwtUtil.getToken(request) ?: throw CustomizeException(
                StatusCode.UNAUTHORIZED.code(),
                StatusCode.UNAUTHORIZED.message()
            )
            return addComment(comment, userDto, request)

        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }

    /**
     * 用户删除自己所写的评论
     */
    override fun deleteComments(
        request: HttpServletRequest,
        commentReplyDto: CommentReplyVoToDto
    )
            : Result<PageInfo<CommentVo>> {
        val userDto = jwtUtil.getToken(request) ?: throw CustomizeException(
            StatusCode.UNAUTHORIZED.code(),
            StatusCode.UNAUTHORIZED.message()
        )
        //查询出当前用户的所有评论
        val userComment = redisTemplate.opsForZSet().rangeWithScores(
            CacheConstants.COMMENTS_USER_ID + userDto.userId,
            0,
            -1
        )
        val level = commentReplyDto.level
        if (userComment.isNullOrEmpty()) {
            //判断是否是一级评论
            if (level == null || level == 0) {
                //是的话先从缓存中删除，之后再从数据库中删除
                //当前用户并没有评论数据，考虑到redis淘汰策略问题，需要再从数据库中查询
                //不考虑数据库无数据的情况，因为是先插入数据库持久化，才插入缓存
                deleteCommentById(commentReplyDto)
            } else {
                //删除二级评论
                deleteCommentReplyById(commentReplyDto)
            }
        } else {
            //判断是否是一级评论
            if (level == null || level == 0) {
                userComment.forEach outer@{
                    val commentAndReply = it.value as CommentReplyVo
                    if (commentReplyDto.id == commentAndReply.id && commentAndReply.level == 0) {
                        redisTemplate.opsForZSet().remove(
                            CacheConstants.COMMENTS_USER_ID + userDto.userId,
                            commentAndReply
                        )
                        return@outer
                    }
                }
                deleteCommentById(commentReplyDto)
            } else {
                userComment.forEach outer@{
                    val commentAndReply = it.value as CommentReplyVo
                    if (commentReplyDto.id == commentAndReply.id && commentAndReply.level == 1) {
                        redisTemplate.opsForZSet().remove(
                            CacheConstants.COMMENTS_USER_ID + userDto.userId,
                            commentAndReply
                        )
                        return@outer
                    }
                }
                deleteCommentReplyById(commentReplyDto)
            }
        }
        //默认返回第一页，每页显示10个
        return findByUserComments(1, 10, request)
    }

    /**
     * 查询当前用户的所写评论
     * start: 开始页码 ,end: 每页显示行数
     */
    override fun findByUserComments(start: Int, end: Int, request: HttpServletRequest)
            : Result<PageInfo<CommentVo>> {
        val userDto = jwtUtil.getToken(request) ?: throw CustomizeException(
            StatusCode.NOT_FOUND_COMMENT.code(),
            StatusCode.NOT_FOUND_COMMENT.message()
        )
        val userId = userDto.userId
        val commentUser = CacheConstants.COMMENTS_USER_ID + userId
        //获取评论总数
        val count = redisTemplate.opsForZSet().size(commentUser)
        if (count == null || count == 0L) {
            //如果redis没有评论则从数据库中查询
            val pageInfo = selectUserComments(start, end, userDto)
            return Result.ok(pageInfo)
        }
        val totalPages = if ((count % end.toLong()) == 0L) count / end.toLong()
        else (count / end.toLong()) + 1
        //开始索引
        val pageStart = (start.toLong() - 1) * end
        //结束索引
        val pageEnd = pageStart + end - 1
        //从redis里面查询出来当前用户自己的评论
        val redisUserCommentSet = redisTemplate.opsForZSet().range(
            commentUser,
            pageStart,
            pageEnd
        )
        val commentList = mutableListOf<CommentVo>()
        redisUserCommentSet!!.forEach {
            val f = it as Comment
            val commentVo = CommentVo().apply {
                this.id = f.id
                this.nickName = f.nickName
                this.content = f.content
                this.headImage = f.headImage
                this.status = 0
                this.cartoonId = f.cartoonId
                this.chapterId = f.chapterId
                this.likes = f.likes
                this.createdTime = f.createdTime
                this.replys = f.replys
            }
            commentList.add(commentVo)
        }
        return Result.ok(
            PageInfo<CommentVo>(
                start.toLong(),
                end.toLong(),
                count,
                totalPages,
                commentList
            )
        )
    }

    /**
     * 根据评论id删除一级评论
     */
    fun deleteCommentById(commentReplyDto: CommentReplyVoToDto) {
        transactionTemplate.execute {
            try {
                val i = this.baseMapper!!.deleteById(commentReplyDto.id)
                if (i <= 0) throw RuntimeException()
            } catch (e: Exception) {
                it.setRollbackOnly()
                throw CustomizeException(
                    StatusCode.DELETE_COMMENT_FAIL.code(),
                    StatusCode.DELETE_COMMENT_FAIL.message()
                )
            }
        }
    }

    /**
     * 根据评论id删除二级评论
     */
    fun deleteCommentReplyById(commentReplyDto: CommentReplyVoToDto) {
        transactionTemplate.execute {
            try {
                val i = commentReplyService.baseMapper!!.deleteById(commentReplyDto.id)
                if (i <= 0) throw RuntimeException()
            } catch (e: Exception) {
                it.setRollbackOnly()
                throw CustomizeException(
                    StatusCode.DELETE_COMMENT_FAIL.code(),
                    StatusCode.DELETE_COMMENT_FAIL.message()
                )
            }

        }

    }

    /**
     * 从数据库中查询当前用户的所有评论
     *
     * 一级评论删除的情况下，有可能二级评论还会在。所以评论全部查询出来然后根据时间排序，再分页
     *
     * (考虑到一个人的评论不会太多，后期可以加堆内存或者表优化)
     */
    private fun selectUserComments(start: Int, end: Int, userDto: UserDto)
            : PageInfo<CommentVo> {
        val commentLists = mutableListOf<CommentVo>()
        //一级评论
        val qw = QueryWrapper<Comment>()
        qw.eq("user_id", userDto.userId)
        val commentList = this.baseMapper!!.selectList(qw)
        if (commentList.isNotEmpty()
            &&
            commentList.size > 0
        ) beanUtils.copyCommentAndCommentVo(
            commentList as List<Comment>, commentLists
        )

        //回复评论
        val qwR = QueryWrapper<CommentReply>()
        qwR.eq("user_id", userDto.userId)
        val commentReplyList = commentReplyService.list(qwR)
        if (commentReplyList.isNotEmpty()
            &&
            commentReplyList.size > 0
        ) beanUtils.copyCommentReplyAndCommentVo(
            commentReplyList as List<CommentReply>,
            commentLists
        )
        if (commentLists.isNullOrEmpty()) throw CustomizeException(
            StatusCode.NOT_FOUND_COMMENT.code(),
            StatusCode.NOT_FOUND_COMMENT.message()
        )

        commentLists.sortByDescending { it.createdTime }
        //开始索引
        val pageStart = (start - 1) * end
        //结束索引
        val pageEnd = pageStart + end - 1
        //总记录数
        val total = commentLists.size.toLong()
        val totalPages = if (total % end == 0L) total / end
        else (total / end) + 1
        val list = commentLists.subList(pageStart, pageEnd)
        return PageInfo<CommentVo>(
            start.toLong(),
            end.toLong(),
            total,
            totalPages,
            list
        )
    }

    /**
     * 获取当前漫画的一级评论，分页显示
     */
    private fun getComment(
        pageRequestDto: PageRequestDto<CommentConDto?>,
        userDto: UserDto?
    ): Result<PageInfo<CommentVo>> {
        //我并没有设置过期时间，因为淘汰策略，导致redis数据满的时候，会存在数据丢失，
        //有一种情况就是redis里面数据丢失后，数据库数据还在
        //不能直接在redis里面查询出来没有数据才去数据库，也不能直接从数据库查询，
        //因为凌晨2点redis的评论数据才同步到数据库，而是数据库和redis一起查询，
        //如果评论id数量一致说明没有数据库丢失，如果评论id数量不一致，说明有一方数据不全，选择从评论id数量多的地方查询
        val redisCommentList = getRedisCommentsByPage(pageRequestDto, userDto)
        val sqlCommentList = getCommentBySql(pageRequestDto, userDto)
        //比较数量
        val i = compareComments(redisCommentList.data.list, sqlCommentList.list)
        when (i) {
            //redis多，则从redis里面查询出来
            0 -> {
                return redisCommentList
            }
            //db多，则从数据库里面查询出来
            1 -> {
                return Result.ok(sqlCommentList)
            }
            //相等，则从redis里面查询出来
            2 -> {
                return redisCommentList
            }
            //两边都没有评论，则返回空
            else -> {
                return Result.ok()
            }
        }
    }

    /**
     * 比较redis的评论多还是db的评论多
     *
     * 0:redis多  1:db多  2:相等 3:两边都没有评论
     */
    private fun compareComments(r: List<CommentVo>, s: List<CommentVo>): Int {
        if (r.isNullOrEmpty()) {
            if (s.isNullOrEmpty()) {
                //说明当前漫画无值
                return 3
            } else {
                //以数据库的评论数据为准
                return 1
            }
        } else {
            if (s.isNullOrEmpty()) {
                //以redis的评论数据为准
                return 0
            } else {
                //比较数量
                return if (r.size - s.size > 0) {
                    //redis多
                    0
                } else if (r.size - s.size < 0) {
                    //db多
                    1
                } else {
                    //相等
                    2
                }
            }
        }
    }

    /**
     * 从redis里面查询评论
     */
    private fun getRedisCommentsByPage(
        p: PageRequestDto<CommentConDto?>,
        userDto: UserDto?
    ): Result<PageInfo<CommentVo>> {
        val commentList = mutableListOf<CommentVo>()
        val pageCurrent = p.page
        val pageSize = p.size
        val data = p.body
        val start = (pageCurrent - 1) * pageSize.toLong()
        val end = start + pageSize.toLong() - 1
        //从redis里面查询评论 分页查询
        val redisResult = redisTemplate.opsForZSet()
            .reverseRangeByScore(
                CacheConstants.COMMENTS
                        + data!!.cartoonId
                        + data.chapterId,
                //最小分数
                0.0,
                //最大分数
                System.currentTimeMillis().toDouble(),
                //表示从第几个元素开始
                start,
                //表示返回多少个元素
                end
            )
        //如果redis没有数据，返回空值
        if (redisResult.isNullOrEmpty()) Result.ok<PageInfo<CommentVo>>()
        //如果redis里面有值，那么就计算总量(总量只计算一级评论)
        val total = redisResult!!.size.toLong()
        //计算总页数
        val totalPages = 0L
        if (total != 0L) {
            if (total % pageSize == 0L) total / pageSize
            else (total / pageSize) + 1
        }
        //遍历每一个一级评论
        redisResult.forEach {
            val commentVo = CommentVo()
            val com = it as Comment
            //获取每一个一级评论下的所有子评论
            val commentReplyVoList = commentReplyService.getCommentsByPageMethod(
                //构建回复评论的dto
                CommentReplyDto().apply {
                    this.cartoonId = com.cartoonId
                    this.chapterId = com.chapterId
                    this.commentId = com.id
                    this.userId = com.userId
                })
            beanUtils.copyCommentAndCommentVo(com, commentVo, userDto)
            //如果一级评论下没有回复评论，则直接添加到评论列表中
            if (commentReplyVoList.size <= 0
                &&
                commentReplyVoList.isNullOrEmpty()
            ) commentList.add(commentVo)
            else {
                //如果有回复评论则添加到一级评论中
                commentVo.childrenCommentList = commentReplyVoList
                commentList.add(commentVo)
            }
        }
        return Result.ok(
            PageInfo<CommentVo>(
                pageCurrent,
                pageSize,
                total,
                totalPages,
                commentList
            )
        )
    }

    /**
     * 章节id为不为0，都是插入到评论表中，不需要分出两个方法
     */
    private fun addComment(
        commentCon: CommentConDto,
        userDto: UserDto,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentVo>> {
        val cartoonId = commentCon.cartoonId
        val chapterId = commentCon.chapterId
        val content = commentCon.content
        val comment = Comment().apply {
            this.userId = userDto.userId.toInt()
            this.nickName = userDto.user.nickname
            this.headImage = userDto.user.avatar
            this.likes = 0
            this.cartoonId = cartoonId
            this.chapterId = chapterId
            this.content = content
        }

        /*
        取消对redis和数据库同时进行事务控制，取消凌晨2点期间的redis任务调度器定时同步
        改为先插入数据库后插入缓存，适用于大部分场景(评论一般都是读多写少状态)
        如果缓存插入失败，则在查看评论期间进行缓存回填
        插入数据库后失败则不进行重试以及接下来的代码流程
         */
        /*transactionTemplate.execute {
            try {
                sqlInsertReply(comment)
                redisInsertReply(comment)
            } catch (e: Exception) {
                it.setRollbackOnly()
                logger.error("回复评论事务插入失败，进行回滚,{}", e.message)
                redisTemplate.opsForZSet().remove(
                    CacheConstants.COMMENTS
                            + comment.cartoonId
                            + comment.chapterId,
                    comment
                )
                throw CustomizeException(
                    StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                    StatusCode.TRANSACTION_INSERTION_FAILED.message()
                )
            }
        }*/
        sqlInsertReply(comment)
        redisInsertReply(comment)
        //整合分页查询方法，返回评论列表
        //因为每插入一条评论都是排到第一位的(根据时间倒序)，所以在分页这里设置1页显示10个
        return pageComment(
            PageRequestDto(1, 10, CommentConDto().apply {
                this.cartoonId = commentCon.cartoonId
                this.chapterId = commentCon.chapterId
            }), request
        )
    }


    /**
     * 往redis里面插入评论
     */
    private fun redisInsertReply(r: Comment) {
        val add = redisTemplate.opsForZSet().add(
            CacheConstants.COMMENTS
                    + r.cartoonId
                    + r.chapterId,
            r,
            localDateUtils.localDateToDouble(r.createdTime)
        )
        if (add == null || !add) {
            logger.error("redis插入评论数据失败")
            throw CustomizeException(
                StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                StatusCode.TRANSACTION_INSERTION_FAILED.message()
            )
        }
        //当前用户每插入一条评论，就往redis中另外存储一次评论记录
        commentAndReplyService.addRedisUserComments(r)
    }

    /**
     * 往数据库插入评论
     */
    private fun sqlInsertReply(r: Comment): Comment {
        transactionTemplate.execute {
            try {
                val flag = this.save(r)
                if (!flag) throw RuntimeException()
            } catch (e: Exception) {
                it.setRollbackOnly()
                logger.error("数据库插入评论数据失败")
                throw CustomizeException(
                    StatusCode.NOT_FOUND.code(),
                    StatusCode.NOT_FOUND.message()
                )
            }
        }
        return r
    }

    /**
     * 通过数据库分页查询获取当前漫画的所有评论
     */
    private fun getCommentBySql(
        pageRequestDto: PageRequestDto<CommentConDto?>,
        userDto: UserDto?
    ): PageInfo<CommentVo> {
        val pageCurrent = pageRequestDto.page
        val pageSize = pageRequestDto.size
        val data = pageRequestDto.body
        val page = Page<Comment>(pageCurrent, pageSize)
        val qw = QueryWrapper<Comment>()
        qw.eq("cartoon_id", data!!.cartoonId)
        qw.eq("chapter_id", data.chapterId)
        this.page(page, qw)
        val records = page.records
        val total = page.total
        val totalPage = page.pages
        val commentList = mutableListOf<CommentVo>()
        //遍历一级评论列表，获取一级评论的回复评论，
        //这里不考虑里面有没有值，因为在上级方法中的别处做了校验
        records.forEach outer@{
            val commentVo = CommentVo()
            val qwR = QueryWrapper<CommentReply>()
            qwR.eq("comment_id", it.id)
            val replyList = commentReplyService.baseMapper.selectList(qwR)
            beanUtils.copyCommentAndCommentVo(it, commentVo, userDto)
            if (replyList.isNullOrEmpty()) return@outer
            val replyVoList = mutableListOf<CommentReplyVo>()
            beanUtils.copyCommentReplyAndReplyVo(
                replyList as Collection<Any>, replyVoList, userDto!!.userId.toInt()
            )
            commentVo.childrenCommentList = replyVoList
            commentList.add(commentVo)
        }
        return PageInfo(
            pageCurrent,
            pageSize,
            total,
            totalPage,
            commentList
        )
    }
}
