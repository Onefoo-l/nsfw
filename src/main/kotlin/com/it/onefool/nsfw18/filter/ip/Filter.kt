package com.it.onefool.nsfw18.filter.ip

import com.it.onefool.nsfw18.domain.entry.OperationLog
import org.slf4j.LoggerFactory

/**
 * @Author linjiawei
 * @Date 2024/6/30 3:24
 */
abstract class Filter {
    companion object{
        private val log = LoggerFactory.getLogger(Filter::class.java)
    }

    abstract fun doFilter(operation: OperationLog)

    abstract fun addOperation(operation: OperationLog)
}