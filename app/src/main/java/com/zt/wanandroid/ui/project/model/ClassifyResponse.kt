package com.zt.wanandroid.ui.project.model

import com.google.gson.annotations.SerializedName

/**
 * 分类数据模型（项目分类、公众号分类等）
 */
data class ClassifyResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("visible")
    val visible: Int = 0,
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("parentChapterId")
    val parentChapterId: Int = 0,
    @SerializedName("children")
    val children: List<ClassifyResponse>? = null
)

