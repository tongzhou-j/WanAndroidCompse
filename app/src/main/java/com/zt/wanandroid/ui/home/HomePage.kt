package com.zt.wanandroid.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.zt.wanandroid.R
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.home.model.BannerResponse
import com.zt.wanandroid.ui.home.viewmodel.HomeUiState
import com.zt.wanandroid.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

/**
 * 首页
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePage(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val banners by viewModel.banners.collectAsState()
    val articles by viewModel.articles.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标题栏
        HomeTopBar()
        
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.colorPrimary)
                    )
                }
            }
            is HomeUiState.Success -> {
                // Banner 和文章列表整合在一起
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 轮播图作为第一个 item
                    if (banners.isNotEmpty()) {
                        item {
                            BannerSection(banners = banners)
                        }
                    }
                    
                    // 文章列表
                    items(articles) { article ->
                        ArticleItemView(article = article)
                    }
                }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as HomeUiState.Error).message,
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
 * 首页顶部标题栏
 */
@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colorResource(id = R.color.colorPrimary))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 首页标题
        Text(
            text = "首页",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
        
        // 搜索图标
       Image(
        painter = painterResource(id = R.mipmap.ic_search),
        contentDescription = "搜索",
        modifier = Modifier
            .size(24.dp)
            .clickable { /* 处理搜索点击事件 */ }
       )    
    }
}

/**
 * 轮播图区域
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerSection(banners: List<BannerResponse>) {
    if (banners.isEmpty()) return
    
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )
    
    // 自动轮播
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.White)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = banners[page].imagePath,
                contentDescription = banners[page].title,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { /* 处理点击事件 */ },
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.mipmap.default_project_img)
            )
        }
        
        // 指示器
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                Color.White
                            else
                                Color.White.copy(alpha = 0.5f)
                        )
                )
                if (index < banners.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

/**
 * 文章列表项
 */
@Composable
fun ArticleItemView(article: Article) {
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
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Text(
                    text = article.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 底部信息栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 分类
                    Text(
                        text = article.getCategoryName(),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.colorPrimary)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // 作者
                    Text(
                        text = article.getAuthorName(),
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
                
                // 时间
                Text(
                    text = article.niceDate,
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
            }
        }
    }
}
