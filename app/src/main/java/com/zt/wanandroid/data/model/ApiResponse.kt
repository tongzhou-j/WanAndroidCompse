package com.zt.wanandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * API 统一响应包装类
 */
data class ApiResponse<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
) {
    /**
     * 判断请求是否成功
     */
    fun isSuccess(): Boolean = errorCode == 0
}

