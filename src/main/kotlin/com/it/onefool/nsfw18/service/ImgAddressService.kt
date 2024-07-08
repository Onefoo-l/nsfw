package com.it.onefool.nsfw18.service
import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.domain.entry.ImgAddress

/**
 * @author 97436
 * @description 针对表【img_address】的数据库操作Service
 * @createDate 2024-06-25 16:51:45
 */
interface ImgAddressService : IService<ImgAddress?> {
    /**
     * 根据图片id查询图片地址
     */
    fun findByImgId(list: List<Int>) : List<ImgAddress>


}
