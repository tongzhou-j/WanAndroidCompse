package com.zt.wanandroid.ui.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zt.wanandroid.data.repository.ProjectRepository
import com.zt.wanandroid.ui.project.model.ClassifyResponse
import com.zt.wanandroid.ui.project.model.ProjectResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 项目页面 ViewModel
 */
class ProjectViewModel : ViewModel() {
    
    private val repository = ProjectRepository()
    
    // UI 状态
    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Loading)
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()
    
    // 分类列表
    private val _classifyList = MutableStateFlow<List<ClassifyResponse>>(emptyList())
    val classifyList: StateFlow<List<ClassifyResponse>> = _classifyList.asStateFlow()
    
    // 项目列表
    private val _projectList = MutableStateFlow<List<ProjectResponse>>(emptyList())
    val projectList: StateFlow<List<ProjectResponse>> = _projectList.asStateFlow()
    
    // 当前页码
    private var currentPage = 0
    
    // 当前分类ID
    private var currentCid = -1
    
    init {
        loadProjectClassify()
    }
    
    /**
     * 加载项目分类
     */
    fun loadProjectClassify() {
        viewModelScope.launch {
            _uiState.value = ProjectUiState.Loading
            
            repository.getProjectClassify().fold(
                onSuccess = { classifyList ->
                    _classifyList.value = classifyList
                    
                    // 默认加载第一个分类的数据
                    if (classifyList.isNotEmpty()) {
                        currentCid = classifyList[0].id
                        loadProjectList(currentCid)
                    } else {
                        _uiState.value = ProjectUiState.Success
                    }
                },
                onFailure = { error ->
                    _uiState.value = ProjectUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 根据分类加载项目列表
     */
    fun loadProjectList(cid: Int) {
        viewModelScope.launch {
            _uiState.value = ProjectUiState.Loading
            currentCid = cid
            currentPage = 1
            
            repository.getProjectList(currentPage, cid).fold(
                onSuccess = { projects ->
                    _projectList.value = projects
                    _uiState.value = ProjectUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = ProjectUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 加载最新项目
     */
    fun loadNewProjects() {
        viewModelScope.launch {
            _uiState.value = ProjectUiState.Loading
            currentCid = -1
            currentPage = 0
            
            repository.getNewProjectList(currentPage).fold(
                onSuccess = { projects ->
                    _projectList.value = projects
                    _uiState.value = ProjectUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = ProjectUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
    
    /**
     * 加载更多项目
     */
    fun loadMoreProjects() {
        viewModelScope.launch {
            currentPage++
            
            val result = if (currentCid == -1) {
                repository.getNewProjectList(currentPage)
            } else {
                repository.getProjectList(currentPage, currentCid)
            }
            
            result.fold(
                onSuccess = { projects ->
                    _projectList.value = _projectList.value + projects
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
        if (currentCid == -1) {
            loadNewProjects()
        } else {
            loadProjectList(currentCid)
        }
    }
}

/**
 * 项目页面 UI 状态
 */
sealed class ProjectUiState {
    data object Loading : ProjectUiState()
    data object Success : ProjectUiState()
    data class Error(val message: String) : ProjectUiState()
}

