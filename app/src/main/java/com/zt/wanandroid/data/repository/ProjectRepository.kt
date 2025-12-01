package com.zt.wanandroid.data.repository

import com.zt.wanandroid.network.RetrofitClient
import com.zt.wanandroid.ui.project.model.ClassifyResponse
import com.zt.wanandroid.ui.project.model.ProjectResponse

/**
 * 项目数据仓库
 */
class ProjectRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * 获取项目分类
     */
    suspend fun getProjectClassify(): Result<List<ClassifyResponse>> {
        return try {
            val response = apiService.getProjectClassify()
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
     * 根据分类id获取项目列表
     */
    suspend fun getProjectList(page: Int, cid: Int): Result<List<ProjectResponse>> {
        return try {
            val response = apiService.getProjectList(page, cid)
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
     * 获取最新项目
     */
    suspend fun getNewProjectList(page: Int): Result<List<ProjectResponse>> {
        return try {
            val response = apiService.getNewProjectList(page)
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

