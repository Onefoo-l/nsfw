package com.it.onefool.nsfw18.common.controller
import java.io.Serializable
import com.it.onefool.nsfw18.common.Result

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
interface ISelectController<T> {

    //根据ID 获取信息
    fun findById(id: Serializable?): Result<T>?

    //根据ID 获取信息列表
    fun findAll(): Result<List<T>?>?

    //根据条件查询   where xxx=? and yyy=?
    fun findByRecord(record: T): Result<List<T>?>?
}
