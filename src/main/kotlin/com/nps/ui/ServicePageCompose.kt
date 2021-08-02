package com.nps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.nps.common.AppConfigCommon
import com.nps.common.AppLogType
import com.nps.common.CallbackCommon
import com.nps.socket.ServiceSocket
import kotlinx.coroutines.launch

@Composable
fun ServicePageCompose(
    modifier: Modifier = Modifier
) {

    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(key1 = Unit) {
        coroutineScope.launch {
            AppConfigCommon.getConfigData()?.serverConfig?.portAddress?.toIntOrNull()?.let { port ->
                ServiceSocket.startServer(port)
            } ?: let {
                CallbackCommon.logCallback(AppLogType.LogError, "未设置绑定端口，请先设置服务端口号")
            }
        }
        onDispose {
            ServiceSocket.closeServer()
        }
    }

    LogcatCompose(
        modifier = modifier
    )

}