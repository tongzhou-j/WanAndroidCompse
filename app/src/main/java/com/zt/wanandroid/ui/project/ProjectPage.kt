package com.zt.wanandroid.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zt.wanandroid.R

/**
 * 项目页面
 */
@Composable
fun ProjectPage() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val tabs = listOf("最新项目", "热门项目", "开源框架", "完整项目")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标签栏
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.colorPrimary),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = colorResource(id = R.color.colorPrimary)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
        
        // 项目列表
        ProjectList()
    }
}

/**
 * 项目列表
 */
@Composable
fun ProjectList() {
    val projects = remember {
        listOf(
            ProjectItem("Jetpack Compose 学习项目", "包含Compose常用组件示例", "Kotlin"),
            ProjectItem("WanAndroid客户端", "玩Android开源客户端", "Kotlin"),
            ProjectItem("MVVM架构示例", "使用MVVM架构的Android项目", "Kotlin"),
            ProjectItem("Retrofit网络框架", "网络请求封装示例", "Java"),
            ProjectItem("自定义View集合", "常用自定义View实现", "Kotlin")
        )
    }
    
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
fun ProjectItemView(project: ProjectItem) {
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
            Text(
                text = project.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = project.description,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = project.language,
                fontSize = 12.sp,
                color = colorResource(id = R.color.colorPrimary)
            )
        }
    }
}

/**
 * 项目数据类
 */
data class ProjectItem(
    val title: String,
    val description: String,
    val language: String
)

