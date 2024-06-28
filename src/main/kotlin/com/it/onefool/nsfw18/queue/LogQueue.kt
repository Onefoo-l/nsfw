package com.it.onefool.nsfw18.queue

import com.it.onefool.nsfw18.domain.entry.OperationLog
import org.slf4j.LoggerFactory
import java.util.concurrent.LinkedBlockingQueue

/**
 * @Author linjiawei
 * @Date 2024/6/25 18:50
 * 记录操作日志的队列
 */
class LogQueue {
    companion object{
        private val log = LoggerFactory.getLogger(LogQueue::class.java)
        private val LOG_QUEUE = LinkedBlockingQueue<OperationLog>()

        fun addLog(operationLog: OperationLog){
            LOG_QUEUE.offer(operationLog)
        }
        fun queueSize() : Int {
            return LOG_QUEUE.size
        }
        fun removeLog() : OperationLog{
            return LOG_QUEUE.poll()
        }
    }
}