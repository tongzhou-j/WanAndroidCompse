package com.zt.wanandroid.ui.tree.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zt.wanandroid.R
import com.zt.wanandroid.ui.home.model.Article
import com.zt.wanandroid.ui.tree.model.NavigationResponse
import com.zt.wanandroid.utils.ColorUtil

/**
 * 导航页面
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavigationPage(navigationList: List<NavigationResponse>) {
    NavigationList(navigationList = navigationList)
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
                        NavigationTagView(article = article)
                    }
                }
            }
        }
    }
}

/**
 * 导航文章标签视图
 */
@Composable
fun NavigationTagView(article: Article) {
    // 使用 remember 记住每个 article 的颜色，避免重组时颜色变化
    val randomColor = remember(article.id) {
        ColorUtil.randomColor()
    }
    
    // 用于检测按下状态
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 根据按下状态选择背景色（对应 flow_selector.xml）
    val backgroundColor = if (isPressed) {
        colorResource(id = R.color.background)
    } else {
        colorResource(id = R.color.windowBackground)
    }
    
    Box(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { /* 处理点击事件，打开文章链接 */ }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = article.title,
            fontSize = 12.sp,
            color = randomColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

