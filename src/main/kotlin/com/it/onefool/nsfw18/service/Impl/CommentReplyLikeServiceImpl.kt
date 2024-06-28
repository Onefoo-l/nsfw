import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.CommentReplyLike
import com.it.onefool.nsfw18.mapper.CommentReplyLikeMapper
import com.it.onefool.nsfw18.service.CommentReplyLikeService
import com.it.onefool.nsfw18.service.Impl.CommentLikeServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【comment_reply_like】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:38
 */
@Service
class CommentReplyLikeServiceImpl : ServiceImpl<CommentReplyLikeMapper?, CommentReplyLike?>(),
    CommentReplyLikeService {
    companion object {
        private val log = LoggerFactory.getLogger(CommentReplyLikeServiceImpl::class.java)
    }
}
