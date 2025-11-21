package com.zt.wanandroid.ui.home.model

import com.google.gson.annotations.SerializedName

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
 * 文章列表响应
 */
data class ArticleListResponse(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Article>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
)

