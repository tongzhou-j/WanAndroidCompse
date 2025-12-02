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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zt.wanandroid.R
import com.zt.wanandroid.ui.tree.model.SystemChildResponse
import com.zt.wanandroid.ui.tree.model.SystemResponse
import com.zt.wanandroid.utils.ColorUtil

/**
 * 体系页面
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SystemPage(systemList: List<SystemResponse>) {
    TreeList(systemList = systemList)
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
                        ChildTagView(child = child)
                    }
                }
            }
        }
    }
}

/**
 * 二级分类标签视图
 */
@Composable
fun ChildTagView(child: SystemChildResponse) {
    // 使用 remember 记住每个 child 的颜色，避免重组时颜色变化
    val randomColor = remember(child.id) {
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
            ) { /* 处理点击事件 */ }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = child.name,
            fontSize = 12.sp,
            color = randomColor
        )
    }
}

