package com.zt.wanandroid.ui.tree.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zt.wanandroid.data.repository.TreeRepository
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.tree.model.NavigationResponse
import com.zt.wanandroid.ui.tree.model.SystemResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 体系页面 ViewModel
 */
class TreeViewModel : ViewModel() {
    
    private val repository = TreeRepository()
    
    // UI 状态
    private val _uiState = MutableStateFlow<TreeUiState>(TreeUiState.Loading)
    val uiState: StateFlow<TreeUiState> = _uiState.asStateFlow()
    
    // 体系列表
    private val _systemList = MutableStateFlow<List<SystemResponse>>(emptyList())
    val systemList: StateFlow<List<SystemResponse>> = _systemList.asStateFlow()
    
    // 广场文章列表
    private val _squareList = MutableStateFlow<List<Article>>(emptyList())
    val squareList: StateFlow<List<Article>> = _squareList.asStateFlow()
    
    // 导航数据列表
    private val _navigationList = MutableStateFlow<List<NavigationResponse>>(emptyList())
    val navigationList: StateFlow<List<NavigationResponse>> = _navigationList.asStateFlow()
    
    // 每日一问文章列表
    private val _askList = MutableStateFlow<List<Article>>(emptyList())
    val askList: StateFlow<List<Article>> = _askList.asStateFlow()
    
    // 当前页码
    private var currentPage = 0
    
    // 当前显示模式：0-广场，1-每日一问，2-体系，3-导航
    private var currentMode = 0
    
    init {
        // 默认加载广场数据（第一个Tab）
        loadSquareData()
    }
    
    /**
     * 加载广场数据（默认第一页）
     */
    fun loadSquareData() {
        viewModelScope.launch {
            _uiState.value = TreeUiState.Loading
            currentMode = 0
            currentPage = 0
            
            repository.getSquareData().fold(
                onSuccess = { articles ->
                    _squareList.value = articles
                    _uiState.value = TreeUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = TreeUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 加载每日一问数据
     */
    fun loadAskData() {
        viewModelScope.launch {
            _uiState.value = TreeUiState.Loading
            currentMode = 1
            currentPage = 1
            
            repository.getAskData(currentPage).fold(
                onSuccess = { articles ->
                    _askList.value = articles
                    _uiState.value = TreeUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = TreeUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 加载体系数据
     */
    fun loadSystemData() {
        viewModelScope.launch {
            _uiState.value = TreeUiState.Loading
            currentMode = 2
            
            repository.getSystemData().fold(
                onSuccess = { systemList ->
                    _systemList.value = systemList
                    _uiState.value = TreeUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = TreeUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 加载导航数据
     */
    fun loadNavigationData() {
        viewModelScope.launch {
            _uiState.value = TreeUiState.Loading
            currentMode = 3
            
            repository.getNavigationData().fold(
                onSuccess = { navigationList ->
                    _navigationList.value = navigationList
                    _uiState.value = TreeUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = TreeUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 加载更多广场数据
     */
    fun loadMoreSquareData() {
        viewModelScope.launch {
            currentPage++
            
            repository.getSquareData(currentPage).fold(
                onSuccess = { articles ->
                    _squareList.value = _squareList.value + articles
                },
                onFailure = { error ->
                    currentPage-- // 加载失败，恢复页码
                }
            )
        }
    }
    
    /**
     * 加载更多每日一问数据
     */
    fun loadMoreAskData() {
        viewModelScope.launch {
            currentPage++
            
            repository.getAskData(currentPage).fold(
                onSuccess = { articles ->
                    _askList.value = _askList.value + articles
                },
                onFailure = { error ->
                    currentPage-- // 加载失败，恢复页码
                }
            )
        }
    }
    
    /**
     * 刷新数据
     */
    fun refresh() {
        when (currentMode) {
            0 -> loadSquareData()
            1 -> loadAskData()
            2 -> loadSystemData()
            3 -> loadNavigationData()
        }
    }
}

/**
 * 体系页面 UI 状态
 */
sealed class TreeUiState {
    data object Loading : TreeUiState()
    data object Success : TreeUiState()
    data class Error(val message: String) : TreeUiState()
}

