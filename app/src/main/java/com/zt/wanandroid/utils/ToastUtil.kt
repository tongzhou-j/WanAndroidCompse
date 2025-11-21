package com.zt.wanandroid.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 全局提示工具类
 */
object ToastUtil {
    private var toast: Toast? = null
    
    /**
     * 显示短时提示
     */
    fun showShort(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }
    
    /**
     * 显示长时提示
     */
    fun showLong(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast?.show()
    }
}

/**
 * Snackbar 工具类（用于 Compose）
 */
object SnackbarUtil {
    /**
     * 显示 Snackbar
     */
    fun show(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        message: String,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                onAction?.invoke()
            }
        }
    }
}

