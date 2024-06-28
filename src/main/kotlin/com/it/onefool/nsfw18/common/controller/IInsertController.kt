package com.it.onefool.nsfw18.common.controller

import com.it.onefool.nsfw18.common.Result

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
interface IInsertController<T> {
    /**
     * 添加记录
     * @param record
     * @return
     */
    fun insert(record: T): Result<Any>?
}