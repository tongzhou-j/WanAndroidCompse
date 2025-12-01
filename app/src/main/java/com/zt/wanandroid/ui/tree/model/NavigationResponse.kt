package com.zt.wanandroid.ui.tree.model

import com.google.gson.annotations.SerializedName
import com.zt.wanandroid.ui.home.model.Article

/**
 * 导航数据模型
 */
data class NavigationResponse(
    @SerializedName("articles")
    val articles: List<Article> = emptyList(),
    @SerializedName("cid")
    val cid: Int = 0,
    @SerializedName("name")
    val name: String = ""
)

