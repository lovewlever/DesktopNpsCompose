package com.nps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nps.common.AppConfigCommon
import com.nps.model.AppConfigData
import kotlinx.coroutines.launch

/**
 * Config设置
 */
@Composable
fun AppConfigSettingCompose(
    modifier: Modifier = Modifier
) {

    val appConfigDataState = remember {
        mutableStateOf(AppConfigData())
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        appConfigDataState.value = AppConfigCommon.getConfigData() ?: AppConfigData()
    }

    Surface {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                ServerSettingCompose(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    appConfigDataState = appConfigDataState
                )

                Spacer(
                    modifier = Modifier
                        .width(0.7.dp)
                        .fillMaxHeight()
                        .background(
                            color = Color(0xFFDEDEDE)
                        )
                )

                ClientSettingCompose(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    appConfigDataState = appConfigDataState
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    coroutineScope.launch {
                        AppConfigCommon.putConfig(appConfigDataState.value)
                        appConfigDataState.value = AppConfigCommon.getConfigData() ?: AppConfigData()
                    }

                }
            ) {
                Text(text = "修改")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}

@Composable
private fun ServerSettingCompose(
    modifier: Modifier = Modifier,
    appConfigDataState: MutableState<AppConfigData>
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "服务端设置",
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                value = appConfigDataState.value.serverConfig.portAddress,
                onValueChange = { appConfigDataState.value.serverConfig.portAddress = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text(text = "监听端口")
                }
            )
        }
    }
}

@Composable
private fun ClientSettingCompose(
    modifier: Modifier = Modifier,
    appConfigDataState: MutableState<AppConfigData>
) {

    val settingList = arrayOf(
        "远程访问IP",
        "远程访问端口",
        "npcParam",
        "远程根目录",
        "文件下载保存目录"
    )

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "客户端设置",
                style = MaterialTheme.typography.h5,
            )
        }
        itemsIndexed(settingList) { index, item ->
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                value = when (index) {
                    0 -> appConfigDataState.value.clientConfig.ipAddress
                    1 -> appConfigDataState.value.clientConfig.portAddress
                    2 -> appConfigDataState.value.clientConfig.npcParam
                    3 -> appConfigDataState.value.clientConfig.remoteRootPath
                    4 -> appConfigDataState.value.clientConfig.fileDownloadSavePath
                    else -> ""
                },
                onValueChange = {
                    when (index) {
                        0 -> appConfigDataState.value.clientConfig.ipAddress = it
                        1 -> appConfigDataState.value.clientConfig.portAddress = it
                        2 -> appConfigDataState.value.clientConfig.npcParam = it
                        3 -> appConfigDataState.value.clientConfig.remoteRootPath = it
                        4 -> appConfigDataState.value.clientConfig.fileDownloadSavePath = it
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text(text = item)
                }
            )
        }
    }
}