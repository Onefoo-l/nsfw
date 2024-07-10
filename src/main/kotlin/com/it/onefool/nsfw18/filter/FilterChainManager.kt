package com.it.onefool.nsfw18.filter

import jakarta.servlet.*
import java.io.IOException


/**
 * @Author linjiawei
 * @Date 2024/7/11 3:42
 * 过滤器链管理器
 */
class FilterChainManager : Filter {
    companion object {
        private class InternalFilterChain(
            private val filters: List<CustomFilter>,
            private val originalChain: FilterChain
        ) :
            FilterChain {
            private var currentPosition = 0
            @Throws(IOException::class, ServletException::class)
            override fun doFilter(request: ServletRequest?, response: ServletResponse?) {
                if (currentPosition < filters.size) {
                    val currentFilter = filters[currentPosition++]
                    if (currentFilter.isEnabled()) {
                        // 调用当前过滤器的doFilter方法
                        currentFilter.doFilter(request, response, this)
                    } else {
                        // 调用自身来处理下一个过滤器
                        doFilter(request, response)
                    }
                } else {
                    // 所有过滤器都处理完毕，调用原始的FilterChain来处理请求(过滤器走完了)
                    originalChain.doFilter(request, response)
                }
            }
        }
    }

    private val filters: MutableList<CustomFilter> = ArrayList()
    fun addFilter(filter: CustomFilter) {
        filters.add(filter)
    }


    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        InternalFilterChain(filters, chain!!).doFilter(request, response)
    }

    override fun destroy() {
        for (filter in filters) {
            filter.destroy()
        }
    }


}