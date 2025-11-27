package com.zt.wanandroid.ui.home.model

import com.google.gson.annotations.SerializedName
import com.zt.wanandroid.data.model.ApiPagerResponse

/**
 * 文章数据模型
 */
data class Article(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("shareUser")
    val shareUser: String = "",
    @SerializedName("superChapterName")
    val superChapterName: String = "",
    @SerializedName("chapterName")
    val chapterName: String = "",
    @SerializedName("niceDate")
    val niceDate: String,
    @SerializedName("fresh")
    val fresh: Boolean = false,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("collect")
    val collect: Boolean = false
) {
    /**
     * 获取作者名称（优先显示 author，为空则显示 shareUser）
     */
    fun getAuthorName(): String {
        return author.ifEmpty { shareUser.ifEmpty { "匿名" } }
    }
    
    /**
     * 获取分类名称
     */
    fun getCategoryName(): String {
        return if (superChapterName.isNotEmpty() && chapterName.isNotEmpty()) {
            "$superChapterName · $chapterName"
        } else {
            chapterName.ifEmpty { superChapterName }
        }
    }
}

/**
 * 文章列表响应（使用通用分页数据类）
 */
typealias ArticleListResponse = ApiPagerResponse<List<Article>>

