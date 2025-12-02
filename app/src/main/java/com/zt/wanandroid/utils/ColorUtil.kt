package com.zt.wanandroid.utils

import android.graphics.Color
import androidx.compose.ui.graphics.Color as ComposeColor
import kotlin.random.Random

/**
 * 颜色工具类
 */
object ColorUtil {
    /**
     * 生成随机颜色
     * @return 随机颜色
     */
    fun randomColor(): ComposeColor {
        // 0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = Random.nextInt(190)
        val green = Random.nextInt(190)
        val blue = Random.nextInt(190)
        
        // 使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        val colorInt = Color.rgb(red, green, blue)
        
        // 将 Android Color Int 转换为 Compose Color
        // Color.rgb 返回的是 ARGB 格式的 Int (0xAARRGGBB)，需要转换为 Long
        // 使用 0xFFFFFFFFL 来确保符号扩展正确
        return ComposeColor(colorInt.toLong() and 0xFFFFFFFFL)
    }
}

