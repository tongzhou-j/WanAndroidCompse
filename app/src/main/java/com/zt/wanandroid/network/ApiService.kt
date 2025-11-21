package com.zt.wanandroid.network

import com.zt.wanandroid.data.model.ApiResponse
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.home.model.ArticleListResponse
import com.zt.wanandroid.ui.home.model.BannerResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * WanAndroid API 接口
 */
interface ApiService {
    
    /**
     * 获取首页轮播图
     */
    @GET("banner/json")
    suspend fun getBanners(): ApiResponse<List<BannerResponse>>
    
    /**
     * 获取首页文章列表
     * @param page 页码，从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): ApiResponse<ArticleListResponse>
    
    /**
     * 获取置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticles(): ApiResponse<List<Article>>
}

