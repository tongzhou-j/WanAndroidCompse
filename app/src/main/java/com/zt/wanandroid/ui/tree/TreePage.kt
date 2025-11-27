package com.zt.wanandroid.ui.tree

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zt.wanandroid.R
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.tree.model.NavigationResponse
import com.zt.wanandroid.ui.tree.model.SystemResponse
import com.zt.wanandroid.ui.tree.viewmodel.TreeUiState
import com.zt.wanandroid.ui.tree.viewmodel.TreeViewModel
import kotlinx.coroutines.delay

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
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var animationTrigger by remember { mutableFloatStateOf(0f) }
    
    val tabs = listOf("广场", "每日一问", "体系", "导航")
    
    // 初始化时加载默认Tab（广场）的数据
    LaunchedEffect(Unit) {
        if (squareList.isEmpty()) {
            viewModel.loadSquareData()
        }
    }
    
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
                                
                                // 根据选择的标签加载数据
                                when (index) {
                                    0 -> viewModel.loadSquareData()
                                    1 -> viewModel.loadAskData()
                                    2 -> viewModel.loadSystemData()
                                    3 -> viewModel.loadNavigationData()
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
        
        // 内容区域
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
                when (selectedTabIndex) {
                    0 -> SquareList(articles = squareList)
                    1 -> AskList(articles = askList)
                    2 -> TreeList(systemList = systemList)
                    3 -> NavigationList(navigationList = navigationList)
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


/**
 * 广场文章列表
 */
@Composable
fun SquareList(articles: List<Article>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(articles) { article ->
            SquareArticleItem(article = article)
        }
    }
}

/**
 * 广场文章列表项
 */
@Composable
fun SquareArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { /* 处理点击事件 */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (article.fresh) {
                    Text(
                        text = "新",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(
                                color = Color.Red.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                }
                
                Text(
                    text = article.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    maxLines = 2
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 作者和时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.getAuthorName(),
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (article.getCategoryName().isNotEmpty()) {
                        Text(
                            text = article.getCategoryName(),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.colorPrimary)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    }
                    
                    Text(
                        text = article.niceDate,
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

/**
 * 每日一问文章列表
 */
@Composable
fun AskList(articles: List<Article>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(articles) { article ->
            AskArticleItem(article = article)
        }
    }
}

/**
 * 每日一问文章列表项
 */
@Composable
fun AskArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { /* 处理点击事件 */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (article.fresh) {
                    Text(
                        text = "新",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(
                                color = Color.Red.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                }
                
                Text(
                    text = article.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    maxLines = 2
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 作者和时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.getAuthorName(),
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (article.getCategoryName().isNotEmpty()) {
                        Text(
                            text = article.getCategoryName(),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.colorPrimary)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    }
                    
                    Text(
                        text = article.niceDate,
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

/**
 * 体系列表
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TreeList(systemList: List<SystemResponse>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(systemList) { system ->
            TreeItemView(system = system)
        }
    }
}

/**
 * 体系列表项
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TreeItemView(system: SystemResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { /* 处理点击事件 */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 一级分类标题
            Text(
                text = system.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            
            // 二级分类标签
            if (system.children.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    system.children.forEach { child ->
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .background(
                                    color = colorResource(id = R.color.colorPrimary).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable { /* 处理点击事件 */ }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = child.name,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.colorPrimary)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 导航列表
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavigationList(navigationList: List<NavigationResponse>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(navigationList) { navigation ->
            NavigationItemView(navigation = navigation)
        }
    }
}

/**
 * 导航列表项
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavigationItemView(navigation: NavigationResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 导航分类标题
            Text(
                text = navigation.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            
            // 文章链接标签
            if (navigation.articles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    navigation.articles.forEach { article ->
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .background(
                                    color = colorResource(id = R.color.colorPrimary).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable { /* 处理点击事件，打开文章链接 */ }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = article.title,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.colorPrimary),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

