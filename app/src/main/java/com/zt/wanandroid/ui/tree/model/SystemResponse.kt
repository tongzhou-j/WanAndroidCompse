package com.zt.wanandroid.ui.tree.model

import com.google.gson.annotations.SerializedName

/**
 * 体系数据模型
 */
data class SystemResponse(
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
    val children: List<SystemChildResponse> = emptyList()
)

/**
 * 体系子分类数据模型
 */
data class SystemChildResponse(
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
    val parentChapterId: Int = 0
)

