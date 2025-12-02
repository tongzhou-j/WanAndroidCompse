package com.zt.wanandroid.ui.tree

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zt.wanandroid.R
import com.zt.wanandroid.ui.tree.pages.AskPage
import com.zt.wanandroid.ui.tree.pages.NavigationPage
import com.zt.wanandroid.ui.tree.pages.SquarePage
import com.zt.wanandroid.ui.tree.pages.SystemPage
import com.zt.wanandroid.ui.tree.viewmodel.TreeUiState
import com.zt.wanandroid.ui.tree.viewmodel.TreeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 广场页面
 */
@Composable
fun TreePage(
    viewModel: TreeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val squareList by viewModel.squareList.collectAsState()
    val askList by viewModel.askList.collectAsState()
    val systemList by viewModel.systemList.collectAsState()
    val navigationList by viewModel.navigationList.collectAsState()
    
    val tabs = listOf("广场", "每日一问", "体系", "导航")
    
    // 使用 rememberPagerState 来管理页面状态，支持缓存
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )
    
    var animationTrigger by remember { mutableFloatStateOf(0f) }
    
    // 同步 Pager 页面变化到 Tab 选择
    val selectedTabIndex = pagerState.currentPage
    
    // 初始化时加载默认Tab（广场）的数据
    LaunchedEffect(Unit) {
        if (squareList.isEmpty()) {
            viewModel.loadSquareData()
        }
    }
    
    // 监听页面切换，加载对应数据
    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> {
                if (squareList.isEmpty()) {
                    viewModel.loadSquareData()
                }
            }
            1 -> {
                if (askList.isEmpty()) {
                    viewModel.loadAskData()
                }
            }
            2 -> {
                if (systemList.isEmpty()) {
                    viewModel.loadSystemData()
                }
            }
            3 -> {
                if (navigationList.isEmpty()) {
                    viewModel.loadNavigationData()
                }
            }
        }
    }
    
    // 获取协程作用域
    val coroutineScope = rememberCoroutineScope()
    
    // 获取屏幕宽度
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val sideSpace = screenWidth / 7
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标签栏容器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(id = R.color.colorPrimary)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧空白区域
            Spacer(modifier = Modifier.width(sideSpace))
            
            // 标签栏
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    CustomTreeTabIndicator(
                        tabPositions = tabPositions,
                        selectedTabIndex = pagerState.currentPage,
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
                                animationTrigger += 1f
                                // 使用协程切换到指定页面
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        },
                        text = {
                            Box(
                                modifier = Modifier.padding(horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    fontSize = if (selectedTabIndex == index) 16.sp else 14.sp,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    )
                }
            }
            
            // 右侧固定区域（包含加号图标）
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)  // 在外部添加 padding，不影响内部空间
                    .width(35.dp)
                    .height(56.dp),
//                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_add),
                    contentDescription = "添加",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { /* 处理添加点击 */ },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
        
        // 内容区域 - 使用 HorizontalPager 实现左右滑动切换和页面缓存
        when (uiState) {
            is TreeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.colorPrimary)
                    )
                }
            }
            is TreeUiState.Success -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    // 使用 key 来保持每个页面的状态，实现页面缓存
                    key = { index -> tabs[index] }
                ) { page ->
                    // 根据页面索引显示对应的内容，HorizontalPager 会自动缓存页面状态
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (page) {
                            0 -> SquarePage(articles = squareList)
                            1 -> AskPage(articles = askList)
                            2 -> SystemPage(systemList = systemList)
                            3 -> NavigationPage(navigationList = navigationList)
                        }
                    }
                }
            }
            is TreeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as TreeUiState.Error).message,
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
fun CustomTreeTabIndicator(
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