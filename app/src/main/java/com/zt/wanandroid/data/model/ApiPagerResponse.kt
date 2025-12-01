package com.zt.wanandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 分页数据的基类
 */
data class ApiPagerResponse<T>(
    @SerializedName("datas")
    var datas: T,
    @SerializedName("curPage")
    var curPage: Int = 0,
    @SerializedName("offset")
    var offset: Int = 0,
    @SerializedName("over")
    var over: Boolean = false,
    @SerializedName("pageCount")
    var pageCount: Int = 0,
    @SerializedName("size")
    var size: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Serializable {
    
    /**
     * 数据是否为空
     */
    fun isEmpty() = (datas as? List<*>)?.isEmpty() ?: true
    
    /**
     * 是否为刷新（第一页数据）
     */
    fun isRefresh() = offset == 0
    
    /**
     * 是否还有更多数据
     */
    fun hasMore() = !over
}

