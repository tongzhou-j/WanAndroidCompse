package com.zt.wanandroid.data.repository

import com.zt.wanandroid.network.RetrofitClient
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.home.model.BannerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 首页数据仓库
 */
class HomeRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * 获取轮播图数据
     */
    suspend fun getBanners(): Result<List<BannerResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBanners()
            if (response.isSuccess() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取文章列表
     * @param page 页码
     */
    suspend fun getArticleList(page: Int): Result<List<Article>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getArticleList(page)
            if (response.isSuccess() && response.data != null) {
                Result.success(response.data.datas)
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取置顶文章
     */
    suspend fun getTopArticles(): Result<List<Article>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTopArticles()
            if (response.isSuccess() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

