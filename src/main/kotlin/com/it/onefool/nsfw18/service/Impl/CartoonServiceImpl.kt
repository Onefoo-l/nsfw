package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.CartoonDto
import com.it.onefool.nsfw18.domain.dto.ImgDto
import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.entry.CartoonLabel
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.Label
import com.it.onefool.nsfw18.domain.pojo.FindCartoonConditionType
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CartoonMapper
import com.it.onefool.nsfw18.service.*
import com.it.onefool.nsfw18.strategy.cartoon.ConditionTypeFactory
import com.it.onefool.nsfw18.utils.BeanUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service实现
 * @createDate 2024-06-25 02:49:19
 */
@Service
class CartoonServiceImpl : ServiceImpl<CartoonMapper, Cartoon>(), CartoonService {
    companion object {
        private val logger = LoggerFactory.getLogger(CartoonServiceImpl::class.java)
    }

    @Autowired
    private lateinit var beanUtils: BeanUtils

    @Autowired
    private lateinit var chapterService: ChapterService

    @Autowired
    private lateinit var cartoonLabelService: CartoonLabelService

    @Autowired
    private lateinit var labelService: LabelService

    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var cartoonMapper: CartoonMapper

    @Autowired
    private lateinit var conditionTypeFactory: ConditionTypeFactory

    @Autowired
    private lateinit var imgAddressService: ImgAddressService

    /**
     * 查看漫画id
     */
    override fun findId(id: Int?): Result<CartoonVo> {
        id?.let { i ->
            val cartoonVo = CartoonVo()
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.eq("id", i)
            this.getOne(qwCartoon)?.let {
                // 尽量不用反射，线上影响性能
                //漫画查询
                beanUtils.copyCartoonAndCartoonVo(it, cartoonVo)
            } ?: run { throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()) }
            // 漫画章节查询
            chapterService.findByCartoonId(i).data?.let { c ->
                val chapterIds = c.map { it?.id }
                val qw = QueryWrapper<Chapter>()
                qw.eq("cartoon_id", i)
                chapterIds.forEach { h ->
                    //每个章节查询图片数量
                    qw.eq("chapter_id", h)
                    val imgCount = chapterService.count(qw)
                    cartoonVo.imgCount = imgCount.toInt()
                }
                beanUtils.copyChapterAndCartoonVo(c, cartoonVo)
            }
            //漫画标签查询
            cartoonLabelService.findByCartoonId(i).data?.let { c ->
                val labelIds = c.map { it?.labelId }
                labelService.findById(labelIds).data.let {
                    beanUtils.copyLabelAndCartoonVo(it, cartoonVo)
                }
            }
            // 调用评论业务逻辑层分页查询
            // 回复评论是建立在有评论的情况下
            reflexInfo(i, Comment())?.let { r ->
                commentService.pageComment(r).data?.let {
                    beanUtils.copyCommentAndCartoonVo(it.list, cartoonVo)
                    //调用回复评论业务逻辑层分页查询
                    //TODD 表可以优化，以后重构优化
                    /*reflexInfo(i,CommentReply())?.let {
                        commentReplyService.findByCommentId(i).data?.let { c ->
                            commentReplyBuilder(c, cartoonVo)
                        }
                    }*/
                }
            }
            logger.info("查询漫画信息,{}", cartoonVo.toString())
            return Result.ok(cartoonVo)
        }
        return Result.error()
    }

    /**
     * 查询最近更新的漫画
     */
    override fun findByUpdateTime(pageCarton: PageRequestDto<Cartoon>)
            : Result<PageInfo<Cartoon>> {
        val pageSize = pageCarton.size
        val pageSum = pageCarton.page
        val data = pageCarton.body
        data?.let {
            val page = Page<Cartoon>(pageSize, pageSum)
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.orderByDesc("update_time")
            this.page(page, qwCartoon)
            return Result.ok(
                PageInfo(
                    //当前页
                    page.current,
                    //每页显示条数
                    page.size,
                    //总数
                    page.total,
                    //总页数
                    page.pages,
                    page.records
                )
            )
        }
        return Result.error(StatusCode.NOT_FOUND)
    }

    /**
     * 查询最多人观看的漫画
     */
    override fun findManyRead(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>> {
        val pageSize = pageCarton.size
        val pageSum = pageCarton.page
        val data = pageCarton.body
        data?.let {
            val page = Page<Cartoon>(pageSize, pageSum)
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.orderByDesc("read_count")
            this.page(page, qwCartoon)
            return Result.ok(
                PageInfo(
                    //当前页
                    page.current,
                    //每页显示条数
                    page.size,
                    //总数
                    page.total,
                    //总页数
                    page.pages,
                    page.records
                )
            )
        }
        return Result.error(StatusCode.NOT_FOUND)
    }

    /**
     * 查询最多人收藏的漫画
     */
    override fun findManyCollection(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>> {
        val pageSize = pageCarton.size
        val pageSum = pageCarton.page
        val data = pageCarton.body
        data?.let {
            val page = Page<Cartoon>(pageSize, pageSum)
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.orderByDesc("collection_count")
            this.page(page, qwCartoon)
            return Result.ok(
                PageInfo(
                    //当前页
                    page.current,
                    //每页显示条数
                    page.size,
                    //总数
                    page.total,
                    //总页数
                    page.pages,
                    page.records
                )
            )
        }
        return Result.error(StatusCode.NOT_FOUND)
    }

    /**
     * 查询最多人点赞的漫画
     */
    override fun findManyNice(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>> {
        val pageSize = pageCarton.size
        val pageSum = pageCarton.page
        val data = pageCarton.body
        data?.let {
            val page = Page<Cartoon>(pageSize, pageSum)
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.orderByDesc("nice_count")
            this.page(page, qwCartoon)
            return Result.ok(
                PageInfo(
                    //当前页
                    page.current,
                    //每页显示条数
                    page.size,
                    //总数
                    page.total,
                    //总页数
                    page.pages,
                    page.records
                )
            )
        }
        return Result.error(StatusCode.NOT_FOUND)
    }

    /**
     * 多条件查询(优先级 1.漫画名称 2.漫画作者 3.标签 4.漫画人物)
     * 0:全站查询  1:根据漫画名称查询  2:根据漫画作者查询  3:根据标签查询  4:根据漫画人物查询
     */
    override fun findByCondition(
        str: String,
        pages: Long, //页数
        size: Long, //条数
        //0:全站查询  1:根据漫画名称查询  2:根据漫画作者查询  3:根据标签查询  4:根据漫画人物查询
        // (判断不在dao层做处理，避免数据库做复杂处理)
        type: Int
    ): Result<PageInfo<CartoonVo>> {
        val cartoonVoList = mutableListOf<CartoonVo>()
        // 从第几个开始
        val start = (pages - 1) * size
        val service = conditionTypeFactory.getCartoonService(FindCartoonConditionType.fromInt(type))
        //建议join不超过三张表  分页查询后的Id集合
        val cartoonBoIds = service.findByConditionType(str, start, size)
//        val cartoonBoIds = cartoonMapper.findByCondition(str, start, size)
        if (cartoonBoIds.isEmpty()) throw CustomizeException(
            StatusCode.NOT_FOUND.code(),
            StatusCode.NOT_FOUND.message()
        )
        val cartoonBoList = cartoonMapper.findById(cartoonBoIds)
        beanUtils.copyCartoonBoAndCartoonVo(cartoonBoList, cartoonVoList)
        val cartoonBoCount = cartoonMapper.findByConditionCount(str)
        val totalPages = if ((cartoonBoCount % size).toInt() == 0)
            (cartoonBoCount / size) else (cartoonBoCount / size + 1)
        return Result.ok(
            PageInfo<CartoonVo>(
                pages,
                size,
                cartoonBoCount.toLong(),
                totalPages,
                cartoonVoList
            )
        )
    }

    /**
     * 添加漫画信息
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun addCartoon(cartoonDto: CartoonDto?): Result<String> {
        cartoonDto?.title.let {
            cartoonDto?.avatar.let {
                cartoonDto?.describe.let {
                    cartoonDto?.chapterId.let {
                        cartoonDto?.file.let {
                            var cartoonTitle = cartoonDto!!.title
                            val qw = QueryWrapper<Cartoon>()
                            qw.eq("title", cartoonTitle)
                            this.getOne(qw).let {
                                cartoonTitle += "_1"
                            }
                            //保存漫画表信息
                            val cartoon = Cartoon().apply {
                                title = cartoonTitle
                                avatar = cartoonDto.avatar
                                describe = cartoonDto.describe
                                niceCount = cartoonDto.niceCount ?: 0
                                readCount = cartoonDto.readCount ?: 0
                                collectionCount = cartoonDto.collectionCount ?: 0
                                createTime = LocalDateTime.now()
                                updateTime = LocalDateTime.now()
                            }
                            this.save(cartoon)
                            //保存章节表信息
                            val chapter = Chapter().apply {
                                cartoonId = cartoon.id
                                chapterId = cartoonDto.chapterId
                                chapterName = cartoonDto.chapterName ?: "第${chapterId}章"
                                createTime = LocalDateTime.now()
                                updateTime = LocalDateTime.now()
                            }
                            chapterService.save(chapter)
                            //添加标签
                            val labelIdList = mutableListOf<Int>()
                            cartoonDto.comePeople.let {
                                val comePeopleList = mutableListOf<Label>()
                                cartoonDto.comePeople.forEach { f ->
                                    val comePeople = Label().apply {
                                        comePeople = f
                                    }
                                    comePeopleList.add(comePeople)
                                }
                                labelService.saveBatch(comePeopleList as Collection<Label?>?)
                                comePeopleList.forEach { o -> labelIdList.add(o.id) }
                            }
                            cartoonDto.author.let {
                                val authorList = mutableListOf<Label>()
                                cartoonDto.author.forEach { f ->
                                    val author = Label().apply {
                                        author = f
                                    }
                                    authorList.add(author)
                                }
                                labelService.saveBatch(authorList as Collection<Label?>?)
                                authorList.forEach { o -> labelIdList.add(o.id) }
                            }
                            cartoonDto.workDescription.let {
                                val workDescriptionList = mutableListOf<Label>()
                                cartoonDto.workDescription.forEach { f ->
                                    val workDescription = Label().apply {
                                        workDescription = f
                                    }
                                    workDescriptionList.add(workDescription)
                                }
                                labelService.saveBatch(workDescriptionList as Collection<Label?>?)
                                workDescriptionList.forEach { o -> labelIdList.add(o.id) }
                            }
                            //添加漫画与标签关系表
                            val cartoonLabelList = mutableListOf<CartoonLabel>()
                            labelIdList.forEach { o ->
                                val cartoonLabel = CartoonLabel().apply {
                                    cartoonId = cartoon.id
                                    labelId = o
                                    createTime = LocalDateTime.now()
                                    updateTime = LocalDateTime.now()
                                }
                                cartoonLabelList.add(cartoonLabel)
                            }
                            cartoonLabelService.saveBatch(cartoonLabelList as Collection<CartoonLabel?>?)
                            val files = cartoonDto.file
                            val imgDto = ImgDto().apply {
                                cartoonId = cartoon.id
                                chapterId = chapter.chapterId
                            }
                            imgAddressService.uploadList(files, imgDto)
                        }
                    }
                }
            }
        }

        return Result.error()
    }

    /**
     * 修改漫画信息
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateCartoon(cartoonDto: CartoonDto?): Result<String> {
        if (cartoonDto!!.id == null) throw CustomizeException(
            StatusCode.FAILURE.code(), StatusCode.FAILURE.message()
        )
        val cartoon = Cartoon().apply {
            id = cartoonDto.id
            title = cartoonDto.title ?: ""
            avatar = cartoonDto.avatar ?: ""
            describe = cartoonDto.describe ?: ""
            niceCount = cartoonDto.niceCount ?: 0
            readCount = cartoonDto.readCount ?: 0
            collectionCount = cartoonDto.collectionCount ?: 0
            updateTime = LocalDateTime.now()
            createTime = LocalDateTime.now()
        }
        this.saveOrUpdate(cartoon)
        val chapter = Chapter().apply {
            cartoonId = cartoonDto.id
            chapterId = cartoonDto.chapterId ?: 0
            chapterName = cartoonDto.chapterName ?: ""
            createTime = LocalDateTime.now()
            updateTime = LocalDateTime.now()
        }
        chapterService.saveOrUpdate(chapter)
        val labelIdList = mutableListOf<Int>()
        val qw = QueryWrapper<CartoonLabel>()
        qw.eq("cartoon_id", cartoonDto.id)
        val cartoonLabelList = cartoonLabelService.baseMapper.selectList(qw)
        if (cartoonLabelList.isNotEmpty()) {
            cartoonLabelList.forEach { o ->
                labelIdList.add(o!!.labelId)
                //删除标签和漫画关系表信息
                cartoonLabelService.removeById(o)
            }
            //删除标签表信息
            labelService.removeByIds(labelIdList)
        }
        //需要添加的标签
        val labelIds = mutableListOf<Int>()
        if (cartoonDto.author.isNotEmpty()) {
            val authorList = mutableListOf<Label>()
            cartoonDto.author.forEach { f ->
                val author = Label().apply {
                    author = f
                    createTime = LocalDateTime.now()
                    updateTime = LocalDateTime.now()
                }
                authorList.add(author)
            }
            labelService.saveBatch(authorList as Collection<Label?>?)
            authorList.forEach { a ->
                labelIds.add(a.id)
            }
        }
        if (cartoonDto.comePeople.isNotEmpty()) {
            val comePeopleList = mutableListOf<Label>()
            cartoonDto.comePeople.forEach { f ->
                val comePeople = Label().apply {
                    comePeople = f
                    createTime = LocalDateTime.now()
                    updateTime = LocalDateTime.now()
                }
                comePeopleList.add(comePeople)
            }
            labelService.saveBatch(comePeopleList as Collection<Label?>?)
            comePeopleList.forEach { a ->
                labelIds.add(a.id)
            }
        }
        if (cartoonDto.workDescription.isNotEmpty()) {
            val workDescriptionList = mutableListOf<Label>()
            cartoonDto.workDescription.forEach { f ->
                val workDescription = Label().apply {
                    workDescription = f
                    createTime = LocalDateTime.now()
                    updateTime = LocalDateTime.now()
                }
                workDescriptionList.add(workDescription)
            }
            labelService.saveBatch(workDescriptionList as Collection<Label?>?)
            workDescriptionList.forEach { a ->
                labelIds.add(a.id)
            }
        }
        val cartoonLabelLists = mutableListOf<CartoonLabel>()
        labelIds.forEach { o ->
            val cartoonLabel = CartoonLabel().apply {
                cartoonId = cartoon.id
                labelId = o
                createTime = LocalDateTime.now()
                updateTime = LocalDateTime.now()
            }
            cartoonLabelLists.add(cartoonLabel)
        }
        cartoonLabelService.saveBatch(cartoonLabelLists as Collection<CartoonLabel?>?)
        if (cartoonDto.file != null) {
            val qwC = QueryWrapper<Chapter>()
            qwC.eq("chapter_id", chapter.chapterId)
            qwC.eq("cartoon_id", cartoonDto.id)
            chapterService.baseMapper.selectList(qwC).let { c ->
                c.forEach { f ->
                    chapterService.removeById(f?.id)
                    imgAddressService.removeById(f?.imgAddressId)
                }
            }
            val imgDto = ImgDto().apply {
                chapterId = chapter.chapterId
                cartoonId = cartoonDto.id
            }
            imgAddressService.uploadList(cartoonDto.file, imgDto)
        }
        return Result.ok()
    }


    /*     //构建整个漫画的评论
        fun commentReplyBuilder(reply: List<CommentReply?>, cartoonVo: CartoonVo) {
            val firstLevelComment = cartoonVo.commentDtoList
            //遍历一级评论
            firstLevelComment.forEach {
                val commentS = commentBuilderTree(reply,it)
                //设置二级评论
                cartoonVo.commentDtoList = commentS
            }
        }


        //构建回复评论列表
        fun commentBuilderTree(reply: List<CommentReply?>,comment: CommentDto) : List<CommentDto> {
            val children = mutableListOf<CommentDto>()
            reply.forEach {
                // 二级评论
                if (it?.commentId == comment.id) {
                    val commentDto = CommentDto().apply {
                        id = it?.id
                        userId = it?.userId
                        nickName = it?.nickName
                        headImage = it?.headImage
                        content = it?.content
                        likes = it?.likes
                        userTime = it?.createdTime
                    }
                    children.add(commentDto)
                }
                // 递归调用
                commentBuilderTree(reply,commentDto)
            }
        }*/

    /**
     * 简化代码，必要时刻可以使用反射
     */
    fun <T> reflexInfo(id: Int, t: T): PageRequestDto<T?>? {
        val clazz = t!!::class.java
        val fields = clazz.declaredFields
        fields.forEach {
            if (it.name == "cartoonId") {
                val clazzT = clazz.newInstance()
                val cId = clazz.getDeclaredField("cartoonId")
                cId.isAccessible = true
                cId.set(clazzT, id)
                return PageRequestDto(1, 5, clazzT)
            }
        }
        return PageRequestDto(1, 5, null)
    }
}
