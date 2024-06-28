package com.it.onefool.nsfw18.common.controller
import com.it.onefool.nsfw18.common.Result

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
interface IUpdateController<T> {
    /**
     * 根据对象进行更新 根据ID
     *
     * @param record
     * @return
     */
    fun updateByPrimaryKey(record: T): Result<Any>?
}
