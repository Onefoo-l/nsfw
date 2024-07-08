package com.it.onefool.nsfw18.strategy.cartoon

import com.it.onefool.nsfw18.domain.pojo.FindCartoonConditionType
import com.it.onefool.nsfw18.mapper.CartoonMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/7/8 21:46
 */
@Component
class PeopleConditionType : AbstractCartoonTemplate(){

    companion object{
        private val log = LoggerFactory.getLogger(NameConditionType::class.java)
    }

    @Autowired
    private lateinit var cartoonMapper: CartoonMapper
    override fun support(): FindCartoonConditionType {
        return FindCartoonConditionType.CARTOON_PEOPLE
    }

    override fun findByConditionType(str: String, start: Long, size: Long): List<Int?> {
        return cartoonMapper.findByConditionPeople(str,start,size);
    }
}