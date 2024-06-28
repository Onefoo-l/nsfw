package com.it.onefool.nsfw18.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @Author linjiawei
 * @Date 2024/6/24 21:17
 */
@Component
class MybatisPlusObjectConfig : MetaObjectHandler{
    companion object{
        private val log = LoggerFactory.getLogger(MybatisPlusObjectConfig::class.java)
    }

    /**
     * 插入时自动填充创建时间
     * @param metaObject
     */
    override fun insertFill(metaObject: MetaObject?) {
        val now = LocalDateTime.now()
        this.strictInsertFill(
            metaObject, "createTime",
            { now },
            LocalDateTime::class.java
        ) // 起始版本 3.3.3(推荐)

        this.strictUpdateFill(
            metaObject, "updateTime",
            { now },
            LocalDateTime::class.java
        ) // 起始版本 3.3.3(推荐)


    }

    /**
     * 插入时自动填充更新时间
     * @param metaObject
     */
    override fun updateFill(metaObject: MetaObject?) {
        this.strictUpdateFill(
            metaObject, "updateTime",
            { LocalDateTime.now() },
            LocalDateTime::class.java
        )
    }
}