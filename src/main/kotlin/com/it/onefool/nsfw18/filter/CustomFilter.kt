package com.it.onefool.nsfw18.filter

import jakarta.servlet.Filter

/**
 * @Author linjiawei
 * @Date 2024/7/11 3:07
 */
interface CustomFilter : Filter{
    fun isEnabled(): Boolean
    fun setEnabled(enabled: Boolean)
}