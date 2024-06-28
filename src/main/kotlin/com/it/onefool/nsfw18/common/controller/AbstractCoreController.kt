package com.it.onefool.nsfw18.common.controller

import com.it.onefool.nsfw18.common.Result
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.beans.PropertyDescriptor
import java.io.Serializable
import java.lang.reflect.Field
import kotlin.math.log

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
abstract class AbstractCoreController<T> : ICoreController<T> {

    companion object {
        private val logger = LoggerFactory.getLogger(AbstractCoreController::class.java)
    }

    //调用方的service
    private var coreService: IService<T>? = null

    constructor()
    constructor(coreService: IService<T>?) {
        this.coreService = coreService
    }

    fun setCoreService(coreService: IService<T>?) {
        this.coreService = coreService
    }

    /**
     * 批量删除记录
     * @param ids
     * @return
     */
    @PostMapping("/deleteIds")
    override fun deleteByIds(ids: List<Serializable?>?): Result<Any> {
        val flag = coreService!!.removeBatchByIds(ids)
        return if (!flag) Result.error() else Result.ok()
    }

    /**
     * 删除记录
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    override fun deleteById(@PathVariable(name = "id") id: Serializable?): Result<Any> {
        val flag = coreService!!.removeById(id)
        return if (!flag) Result.error()
        else Result.ok()
    }

    /**
     * 添加记录
     *
     * @param record
     * @return
     */
    @PostMapping
    override fun insert(@RequestBody record: T): Result<Any>? {
        val flag = coreService!!.save(record)
        return if (!flag) Result.error()
        else Result.ok(record)
    }

    //更新数据
    @PutMapping
    override fun updateByPrimaryKey(@RequestBody record: T): Result<Any> {
        val flag = coreService!!.updateById(record)
        return if (!flag) Result.error()
        else Result.ok()
    }

    @GetMapping("/{id}")
    override fun findById(@PathVariable(name = "id") id: Serializable?): Result<T> {
        val t = coreService!!.getById(id)
        return Result.ok(t)
    }

    @GetMapping
    override fun findAll(): Result<List<T>?>? {
        val list = coreService!!.list() as? List<T> ?: emptyList()
        return Result.ok(list)
    }

    //根据条件查询       这个需要用到反射  select * from 表（泛型） where  ?=? and ?=?
    @PostMapping("/listCondition")
    override fun findByRecord(@RequestBody record: T): Result<List<T>?>? {
        val queryWrapper = QueryWrapper<T?>()
        val fields: Array<Field> = record!!::class.java.declaredFields
        fields.forEach {
            val annotation = it.getAnnotation(
                TableField::class.java
            )
            try {
                it.setAccessible(true)
                val value = it[record]
                value?.let {
                    queryWrapper.eq(annotation.value, value)
                }
            } catch (e: IllegalAccessException) {
                logger.error(e.message)
            }
        }
        val list = coreService!!.list(queryWrapper) as? List<T> ?: emptyList()
        return Result.ok(list)
    }

    /**
     * [通用]条件分页查询  特殊需求 你要重写 或者不用 通用controller
     *
     * @param pageRequestDto
     * @return
     */
    @PostMapping(value = ["/search"])
    override fun findByPage(@RequestBody pageRequestDto: PageRequestDto<T>)
            : Result<PageInfo<T>> {
        val page: IPage<T?> = Page<T?>(pageRequestDto.page, pageRequestDto.size)

        //条件 name 查询 非 lamda表达式查询条件
        val queryWrapper: QueryWrapper<T?> = getWrapper(pageRequestDto.body)
        val iPage = coreService?.page(page, queryWrapper)
        val pageInfo: PageInfo<T> = PageInfo(
            iPage?.current,
            iPage?.size,
            iPage?.total,
            iPage?.getPages(),
            iPage?.records
        )
        return Result.ok(pageInfo)
    }

    private fun getWrapper(body: T?): QueryWrapper<T?> {
        val queryWrapper = QueryWrapper<T?>()
        body ?: return queryWrapper
        val declaredFields: Array<Field> = body.javaClass.getDeclaredFields()
        //遇到 id注解 则直接跳过 不允许实现根据主键查询
        //https://www.coder.work/article/2808807
        declaredFields.filter {
            it.isAnnotationPresent(TableId::class.java) || it.name == "serialVersionUID"
        }
            .forEach {
                try {
                    //属性描述器  record.getClass()
                    val propDesc = PropertyDescriptor(it.name, body.javaClass)
                    //获取这个值  先获取读方法的方法对象,并调用获取里面的值
                    val value = propDesc.getReadMethod().invoke(body)
                    //declaredField.setAccessible(true);
                    //Object value = declaredField.get(body);
                    //如果是字符串
                    val annotation = it.getAnnotation(
                        TableField::class.java
                    )
                    //如果传递的值为空则不做处理
                    value?.let {
                        //如是字符串 则用like
                        if (value is String) {
                            queryWrapper.like(annotation.value, value)
                        } else {
                            //否则使用=号
                            queryWrapper.eq(annotation.value, value)
                        }
                    }
                } catch (e: Exception) {
                    logger.error(e.cause!!.localizedMessage)
                }
            }
        return queryWrapper
    }

    //获取分页对象
    protected fun getPageInfo(iPage: IPage<T>): PageInfo<T> {
        return PageInfo<T>(
            iPage.current,
            iPage.size,
            iPage.total,
            iPage.getPages(),
            iPage.records
        )
    }


}
