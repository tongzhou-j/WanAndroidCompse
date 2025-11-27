package com.zt.wanandroid.ui.project.model

import com.google.gson.annotations.SerializedName

/**
 * 项目数据模型
 */
data class ProjectResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("author")
    val author: String = "",
    @SerializedName("shareUser")
    val shareUser: String = "",
    @SerializedName("niceDate")
    val niceDate: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("envelopePic")
    val envelopePic: String = "",
    @SerializedName("projectLink")
    val projectLink: String = "",
    @SerializedName("collect")
    val collect: Boolean = false,
    @SerializedName("chapterName")
    val chapterName: String = "",
    @SerializedName("superChapterName")
    val superChapterName: String = ""
) {
    /**
     * 获取作者名称（优先显示 author，为空则显示 shareUser）
     */
    fun getAuthorName(): String {
        return author.ifEmpty { shareUser.ifEmpty { "匿名" } }
    }
}

/**
 * 项目列表分页响应
 */
data class ProjectListResponse(
    @SerializedName("curPage")
    val curPage: Int = 0,
    @SerializedName("datas")
    val datas: List<ProjectResponse> = emptyList(),
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("over")
    val over: Boolean = false,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("total")
    val total: Int = 0
)

