package com.it.onefool.nsfw18.common.controller

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
interface ICoreController<T> : ISelectController<T>, IInsertController<T>, IPagingController<T>,
    IDeleteController<T>, IUpdateController<T>
