package com.zt.wanandroid.ui.tree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zt.wanandroid.R

/**
 * 体系页面
 */
@Composable
fun TreePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 标题栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "体系",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }
        
        // 体系列表
        TreeList()
    }
}

/**
 * 体系列表
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TreeList() {
    val treeItems = remember {
        listOf(
            TreeItem(
                title = "开发环境",
                children = listOf("Android Studio", "Gradle", "Git", "ADB")
            ),
            TreeItem(
                title = "基础知识",
                children = listOf("四大组件", "View", "动画", "自定义View")
            ),
            TreeItem(
                title = "进阶知识",
                children = listOf("性能优化", "架构设计", "Jetpack", "Kotlin")
            ),
            TreeItem(
                title = "开源库",
                children = listOf("Retrofit", "OkHttp", "Glide", "RxJava")
            ),
            TreeItem(
                title = "项目实战",
                children = listOf("MVVM", "MVP", "组件化", "插件化")
            )
        )
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(treeItems) { item ->
            TreeItemView(item = item)
        }
    }
}

/**
 * 体系列表项
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TreeItemView(item: TreeItem) {
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
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                item.children.forEach { child ->
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
                            text = child,
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.colorPrimary)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 体系数据类
 */
data class TreeItem(
    val title: String,
    val children: List<String>
)

