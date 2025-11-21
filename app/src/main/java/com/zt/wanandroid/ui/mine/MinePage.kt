package com.zt.wanandroid.ui.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zt.wanandroid.R

/**
 * 我的页面
 */
@Composable
fun MinePage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 头部用户信息
        item {
            UserInfoSection()
        }
        
        // 我的积分
        item {
            Spacer(modifier = Modifier.height(12.dp))
            MenuCard(
                items = listOf(
                    MenuItem("我的积分", R.mipmap.jifen),
                    MenuItem("我的收藏", R.mipmap.collect),
                    MenuItem("阅读历史", R.mipmap.ic_history)
                )
            )
        }
        
        // 其他功能
        item {
            Spacer(modifier = Modifier.height(12.dp))
            MenuCard(
                items = listOf(
                    MenuItem("我的文章", R.mipmap.wenzhang),
                    MenuItem("待办清单", R.mipmap.renwu),
                    MenuItem("我的分享", R.mipmap.ic_tag)
                )
            )
        }
        
        // 设置
        item {
            Spacer(modifier = Modifier.height(12.dp))
            MenuCard(
                items = listOf(
                    MenuItem("设置", R.mipmap.shezhi)
                )
            )
        }
    }
}

/**
 * 用户信息区域
 */
@Composable
fun UserInfoSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { /* 处理点击事件 */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.colorPrimary)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            Image(
                painter = painterResource(id = R.mipmap.ic_account),
                contentDescription = "用户头像",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "请登录",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "登录后查看更多内容",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * 菜单卡片
 */
@Composable
fun MenuCard(items: List<MenuItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                MenuItemView(item = item)
                if (index < items.size - 1) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFEEEEEE)
                    )
                }
            }
        }
    }
}

/**
 * 菜单项
 */
@Composable
fun MenuItemView(item: MenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 处理点击事件 */ }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = item.title,
                fontSize = 15.sp,
                color = Color(0xFF333333)
            )
        }
        
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "进入",
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 菜单项数据类
 */
data class MenuItem(
    val title: String,
    val iconRes: Int
)

