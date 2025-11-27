package com.zt.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zt.wanandroid.ui.home.HomePage
import com.zt.wanandroid.ui.mine.MinePage
import com.zt.wanandroid.ui.project.ProjectPage
import com.zt.wanandroid.ui.public.PublicPage
import com.zt.wanandroid.ui.theme.WanAndroidTheme
import com.zt.wanandroid.ui.tree.TreePage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 设置状态栏颜色
        window.statusBarColor = getColor(R.color.colorPrimary)
        // 设置状态栏图标为浅色
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        
        setContent {
            WanAndroidTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    // 定义底部导航项
    val navItems = listOf(
        NavigationItem("首页", R.mipmap.menu_main),
        NavigationItem("项目", R.mipmap.menu_project),
        NavigationItem("广场", R.mipmap.menu_tree),
        NavigationItem("公众号", R.mipmap.menu_public),
        NavigationItem("我的", R.mipmap.menu_me)
    )
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier.height(70.dp)
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index
                    
                    // 颜色过渡动画
                    val tintColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            Color(colorResource(id = R.color.colorPrimary).value)
                        } else {
                            Color(colorResource(id = R.color.colorGray).value)
                        },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        ),
                        label = "color_animation"
                    )
                    
                    // 缩放动画（带弹性效果）
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "scale_animation"
                    )
                    
                    // 透明度动画
                    val alpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.6f,
                        animationSpec = tween(durationMillis = 300),
                        label = "alpha_animation"
                    )
                    
                    NavigationBarItem(
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = item.iconRes),
                                    contentDescription = item.label,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .scale(scale)
                                        .alpha(alpha),
                                    colorFilter = ColorFilter.tint(tintColor)
                                )
                                
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                Text(
                                    text = item.label, 
                                    fontSize = 11.sp,
                                    color = tintColor,
                                    modifier = Modifier
                                        .scale(scale)
                                        .alpha(alpha)
                                )
                            }
                        },
                        label = null,
                        selected = isSelected,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Unspecified,
                            unselectedIconColor = Color.Unspecified,
                            selectedTextColor = Color.Unspecified,
                            unselectedTextColor = Color.Unspecified,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(innerPadding)
        ) {
            when (selectedIndex) {
                0 -> HomePage()
                1 -> ProjectPage()
                2 -> TreePage()
                3 -> PublicPage()
                4 -> MinePage()
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val iconRes: Int
)

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WanAndroidTheme {
        MainScreen()
    }
}