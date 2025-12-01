package com.zt.wanandroid.data.repository

import com.zt.wanandroid.network.RetrofitClient
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.tree.model.NavigationResponse
import com.zt.wanandroid.ui.tree.model.SystemResponse

/**
 * 体系数据仓库
 */
class TreeRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * 获取体系数据
     */
    suspend fun getSystemData(): Result<List<SystemResponse>> {
        return try {
            val response = apiService.getSystemData()
            if (response.isSuccess()) {
                Result.success(response.data ?: emptyList())
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取广场列表数据（默认第一页）
     */
    suspend fun getSquareData(): Result<List<Article>> = getSquareData(0)
    
    /**
     * 获取广场列表数据
     * @param page 页码，从0开始
     */
    suspend fun getSquareData(page: Int): Result<List<Article>> {
        return try {
            val response = apiService.getSquareData(page)
            if (response.isSuccess()) {
                Result.success(response.data?.datas ?: emptyList())
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取导航数据
     */
    suspend fun getNavigationData(): Result<List<NavigationResponse>> {
        return try {
            val response = apiService.getNavigationData()
            if (response.isSuccess()) {
                Result.success(response.data ?: emptyList())
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取每日一问列表数据
     */
    suspend fun getAskData(page: Int): Result<List<Article>> {
        return try {
            val response = apiService.getAskData(page)
            if (response.isSuccess()) {
                Result.success(response.data?.datas ?: emptyList())
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

