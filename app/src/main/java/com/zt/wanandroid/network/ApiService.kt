package com.zt.wanandroid.network

import com.zt.wanandroid.data.model.ApiPagerResponse
import com.zt.wanandroid.data.model.ApiResponse
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.home.model.BannerResponse
import com.zt.wanandroid.ui.project.model.ClassifyResponse
import com.zt.wanandroid.ui.project.model.ProjectListResponse
import com.zt.wanandroid.ui.tree.model.NavigationResponse
import com.zt.wanandroid.ui.tree.model.SystemResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getArticleList(@Path("page") page: Int): ApiResponse<ApiPagerResponse<List<Article>>>
    
    /**
     * 获取置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticles(): ApiResponse<List<Article>>
    
    /**
     * 获取项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectClassify(): ApiResponse<List<ClassifyResponse>>
    
    /**
     * 根据分类id获取项目列表
     * @param page 页码，从1开始
     * @param cid 分类id
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ProjectListResponse>
    
    /**
     * 获取最新项目
     * @param page 页码，从0开始
     */
    @GET("article/listproject/{page}/json")
    suspend fun getNewProjectList(@Path("page") page: Int): ApiResponse<ProjectListResponse>
    
    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getSystemData(): ApiResponse<List<SystemResponse>>
    
    /**
     * 获取广场列表数据
     * @param page 页码，从0开始
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<List<Article>>>
    
    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationData(): ApiResponse<List<NavigationResponse>>
    
    /**
     * 获取每日一问列表数据
     * @param page 页码，从1开始
     */
    @GET("wenda/list/{page}/json")
    suspend fun getAskData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<List<Article>>>
}

