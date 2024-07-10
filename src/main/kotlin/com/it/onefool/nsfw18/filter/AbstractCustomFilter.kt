package com.it.onefool.nsfw18.filter

/**
 * @Author linjiawei
 * @Date 2024/7/11 3:08
 */
abstract class AbstractCustomFilter : CustomFilter{
    // 默认开启过滤器
    private var enabled = true

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}