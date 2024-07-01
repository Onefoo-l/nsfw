package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.OperationLog
import com.it.onefool.nsfw18.mapper.OperationLogMapper
import com.it.onefool.nsfw18.queue.LogQueue
import com.it.onefool.nsfw18.service.OperationLogService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.Schedules
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author 97436
 * @description 针对表【operation_log】的数据库操作Service实现
 * @createDate 2024-06-25 17:21:43
 */
@Service
class OperationLogServiceImpl
    : ServiceImpl<OperationLogMapper?, OperationLog?>(), OperationLogService {
    companion object {
        private val logger = LoggerFactory.getLogger(OperationLogServiceImpl::class.java)
    }

    // 五分钟执行一次 上线后开启
//    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Transactional(rollbackFor = [Exception::class])
    fun saveLog(){
        val list = CopyOnWriteArrayList<OperationLog>()
        // 上线后根据实际情况调整
        if (LogQueue.queueSize() >= 2){
            list.add(LogQueue.removeLog())
            this.saveBatch(list)
        }
    }
}
