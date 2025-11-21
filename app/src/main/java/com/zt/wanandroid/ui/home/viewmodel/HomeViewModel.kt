package com.zt.wanandroid.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zt.wanandroid.data.repository.HomeRepository
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.home.model.BannerResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel
 */
class HomeViewModel : ViewModel() {
    
    private val repository = HomeRepository()
    
    // UI 状态
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // 轮播图数据
    private val _banners = MutableStateFlow<List<BannerResponse>>(emptyList())
    val banners: StateFlow<List<BannerResponse>> = _banners.asStateFlow()
    
    // 文章列表数据
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()
    
    // 当前页码
    private var currentPage = 0
    
    init {
        loadHomeData()
    }
    
    /**
     * 加载首页数据
     */
    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            try {
                // 并行加载轮播图和文章列表
                val bannersResult = repository.getBanners()
                val articlesResult = repository.getArticleList(0)
                
                if (bannersResult.isSuccess && articlesResult.isSuccess) {
                    _banners.value = bannersResult.getOrNull() ?: emptyList()
                    _articles.value = articlesResult.getOrNull() ?: emptyList()
                    _uiState.value = HomeUiState.Success
                    currentPage = 0
                } else {
                    val error = bannersResult.exceptionOrNull() ?: articlesResult.exceptionOrNull()
                    _uiState.value = HomeUiState.Error(error?.message ?: "加载失败")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "未知错误")
            }
        }
    }
    
    /**
     * 加载更多文章
     */
    fun loadMoreArticles() {
        viewModelScope.launch {
            try {
                val result = repository.getArticleList(currentPage + 1)
                if (result.isSuccess) {
                    val newArticles = result.getOrNull() ?: emptyList()
                    _articles.value = _articles.value + newArticles
                    currentPage++
                }
            } catch (e: Exception) {
                // 加载更多失败不改变整体状态
            }
        }
    }
    
    /**
     * 刷新数据
     */
    fun refresh() {
        loadHomeData()
    }
}

/**
 * 首页 UI 状态
 */
sealed class HomeUiState {
    object Loading : HomeUiState()
    object Success : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

