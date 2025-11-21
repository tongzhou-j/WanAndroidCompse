package com.zt.wanandroid.ui.home.model

import com.google.gson.annotations.SerializedName

/**
 * 轮播图数据模型
 */
data class BannerResponse(
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("imagePath")
    val imagePath: String = "",
    @SerializedName("isVisible")
    val isVisible: Int = 0,
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("url")
    val url: String = ""
)

