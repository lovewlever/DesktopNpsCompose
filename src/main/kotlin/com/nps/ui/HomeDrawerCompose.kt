package com.nps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nps.common.*
import kotlinx.coroutines.launch

@Composable
fun HomeDrawerCompose(
    openPageClick: (AppPageNav) -> Unit = {}
) {

    val array = arrayOf(
        "服务端",
        "客户端",
        "设置",
    )

    val clientDialogConfigState = remember {
        mutableStateOf(false)
    }

    val serverDialogConfigState = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    // 客户端设置
    ClientInputAddressAndPortDialogCompose(
        showDialogState = clientDialogConfigState,
        confirmClick = {
            coroutineScope.launch {
                AppConfigCommon.getConfigData()?.clientConfig?.npcParam?.let {
                    //NPCCommon.startNpcClient(it)
                    openPageClick(AppPageNav.TypeClient)
                } ?: let {
                    CallbackCommon.logCallback(AppLogType.LogError, "未设置NpcParam")
                }
            }
        }
    )

    // 服务端设置
    ServerInputServerPortDialogCompose(
        showDialogState = serverDialogConfigState,
        confirmClick = {
            openPageClick(AppPageNav.TypeServer)
        }
    )

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(array) { item ->
            TextButton(
                modifier = Modifier
                    .width(180.dp)
                    .height(45.dp),
                onClick = {
                    when(item) {
                        "服务端" -> serverDialogConfigState.value = true
                        "客户端" -> clientDialogConfigState.value = true
                        "设置" -> openPageClick(AppPageNav.TypeAppConfigSetting)
                    }
                }
            ) {
                Text(text = item)
            }
        }
    }
}