package com.it.onefool.nsfw18.common.controller
import com.it.onefool.nsfw18.common.Result
import java.io.Serializable

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
interface IDeleteController<T> {
    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    fun deleteById(id: Serializable?): Result<Any>?
    fun deleteByIds(ids: List<Serializable?>?): Result<Any>?
}
