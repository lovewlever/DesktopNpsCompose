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

/**
 * 服务端设置
 */
@Composable
private fun ServerSettingCompose(
    modifier: Modifier = Modifier,
    appConfigDataState: MutableState<AppConfigData>
) {

    val serverPortState = remember {
        mutableStateOf(appConfigDataState.value.serverConfig.portAddress)
    }

    serverPortState.value = appConfigDataState.value.serverConfig.portAddress

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
                value = serverPortState.value,
                onValueChange = {
                    serverPortState.value = it
                    appConfigDataState.value.serverConfig.portAddress = it
                },
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

/**
 * 客户端设置
 */
@Composable
private fun ClientSettingCompose(
    modifier: Modifier = Modifier,
    appConfigDataState: MutableState<AppConfigData>
) {

    val settingList = arrayOf(
        "远程访问IP" to mutableStateOf(appConfigDataState.value.clientConfig.ipAddress),
        "远程访问端口" to mutableStateOf(appConfigDataState.value.clientConfig.portAddress),
        "npcParam" to mutableStateOf(appConfigDataState.value.clientConfig.npcParam),
        "远程根目录" to mutableStateOf(appConfigDataState.value.clientConfig.remoteRootPath),
        "文件下载保存目录" to mutableStateOf(appConfigDataState.value.clientConfig.fileDownloadSavePath)
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
            SettingOutlinedTextFieldCompose(
                text = item.second.value,
                label = item.first,
                onChange = {
                    item.second.value = it
                    when (index) {
                        0 -> {
                            appConfigDataState.value.clientConfig.ipAddress = it
                        }
                        1 -> {
                            appConfigDataState.value.clientConfig.portAddress = it
                        }
                        2 -> {
                            appConfigDataState.value.clientConfig.npcParam = it
                        }
                        3 -> {
                            appConfigDataState.value.clientConfig.remoteRootPath = it
                        }
                        4 -> {
                            appConfigDataState.value.clientConfig.fileDownloadSavePath = it
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun SettingOutlinedTextFieldCompose(
    text: String,
    label: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        value = text,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        label = {
            Text(text = label)
        }
    )
}