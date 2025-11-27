package com.zt.wanandroid.ui.project

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.zt.wanandroid.R
import com.zt.wanandroid.ui.project.model.ProjectResponse
import com.zt.wanandroid.ui.project.viewmodel.ProjectUiState
import com.zt.wanandroid.ui.project.viewmodel.ProjectViewModel
import kotlinx.coroutines.delay

/**
 * 项目页面
 */
@Composable
fun ProjectPage(
    viewModel: ProjectViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val classifyList by viewModel.classifyList.collectAsState()
    val projectList by viewModel.projectList.collectAsState()
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var animationTrigger by remember { mutableFloatStateOf(0f) }
    
    // 构建标签列表：最新项目 + 分类列表
    val tabs = remember(classifyList) {
        listOf("最新项目") + classifyList.map { it.name }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标签栏
        if (tabs.isNotEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.height(56.dp),
                containerColor = colorResource(id = R.color.colorPrimary),
                contentColor = Color.White,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    CustomTabIndicator(
                        tabPositions = tabPositions,
                        selectedTabIndex = selectedTabIndex,
                        animationTrigger = animationTrigger
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            if (selectedTabIndex != index) {
                                selectedTabIndex = index
                                animationTrigger += 1f
                                
                                // 切换标签时加载对应数据
                                if (index == 0) {
                                    viewModel.loadNewProjects()
                                } else {
                                    val classify = classifyList[index - 1]
                                    viewModel.loadProjectList(classify.id)
                                }
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = if (selectedTabIndex == index) 16.sp else 14.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    )
                }
            }
        }
        
        // 内容区域
        when (uiState) {
            is ProjectUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.colorPrimary)
                    )
                }
            }
            is ProjectUiState.Success -> {
                ProjectList(projects = projectList)
            }
            is ProjectUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as ProjectUiState.Error).message,
                            fontSize = 14.sp,
                            color = Color(0xFF999999)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "点击重试",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.colorPrimary),
                            modifier = Modifier.clickable {
                                viewModel.refresh()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 自定义标签指示器
 */
@Composable
fun CustomTabIndicator(
    tabPositions: List<TabPosition>,
    selectedTabIndex: Int,
    animationTrigger: Float
) {
    val currentTabPosition = tabPositions[selectedTabIndex]
    
    // 指示器拉伸动画效果
    var stretchFactor by remember { mutableFloatStateOf(1f) }
    
    // 监听动画触发器，执行拉伸动画
    LaunchedEffect(animationTrigger) {
        if (animationTrigger > 0) {
            // 先拉伸到1.2倍
            stretchFactor = 1.2f
            delay(100)
            // 回到原来大小
            stretchFactor = 1f
        }
    }
    
    val indicatorWidth by animateFloatAsState(
        targetValue = (currentTabPosition.width.value / 3) * stretchFactor,
        animationSpec = tween(durationMillis = 100, easing = LinearEasing),
        label = "indicatorWidth"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = currentTabPosition.left + (currentTabPosition.width - (currentTabPosition.width / 3)) / 2)
            .width(indicatorWidth.dp)
            .height(3.dp)
            .background(Color.White, RoundedCornerShape(1.5.dp))
    )
}

/**
 * 项目列表
 */
@Composable
fun ProjectList(projects: List<ProjectResponse>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(projects) { project ->
            ProjectItemView(project = project)
        }
    }
}

/**
 * 项目列表项
 */
@Composable
fun ProjectItemView(project: ProjectResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { /* 处理点击事件 */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 项目图片
            if (project.envelopePic.isNotEmpty()) {
                AsyncImage(
                    model = project.envelopePic,
                    contentDescription = project.title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            // 项目信息
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // 标题
                Text(
                    text = project.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 描述
                Text(
                    text = project.desc,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 作者和时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.getAuthorName(),
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = project.niceDate,
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

