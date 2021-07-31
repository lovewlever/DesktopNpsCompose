package com.nps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nps.common.AppConfigCommon
import com.nps.model.AppConfigData
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * 客户端 输入Ip，端口，npc参数
 */
@Composable
fun ClientInputAddressAndPortDialogCompose(
    showDialogState: MutableState<Boolean>,
    confirmClick: (config: AppConfigData) -> Unit = {  }
) {
    if (showDialogState.value) {

        var ipAddress by remember {
            mutableStateOf("")
        }

        var portAddress by remember {
            mutableStateOf("")
        }

        var npcParam by remember {
            mutableStateOf("")
        }

        LaunchedEffect(key1 = Unit) {
            val acd = AppConfigCommon.getConfigData() ?: AppConfigData()
            ipAddress = acd.clientConfig.ipAddress
            portAddress = acd.clientConfig.portAddress
            npcParam = acd.clientConfig.npcParam
        }

        val coroutineScope = rememberCoroutineScope()

        Dialog(
            onDismissRequest = {
                showDialogState.value = false
            }
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "请输入地址和端口;")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "IP:")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.width(160.dp),
                            maxLines = 1,
                            singleLine = true,
                            value = ipAddress,
                            onValueChange = { ipAddress = it },
                            label = {
                                Text(text = "连接地址")
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = ":")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.width(80.dp),
                            maxLines = 1,
                            singleLine = true,
                            value = portAddress,
                            onValueChange = { portAddress = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            label = {
                                Text(text = "端口")
                            }
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            singleLine = true,
                            value = npcParam,
                            onValueChange = { npcParam = it },
                            label = {
                                Text(text = "请输入NPC连接参数")
                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                                showDialogState.value = false
                            }
                        ) {
                            Text(text = "取消")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                                val pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")
                                val portPattern = Pattern.compile("^\\d+\$")
                                if (pattern.matcher(ipAddress).matches() && portPattern.matcher(portAddress).matches()) {
                                    // 设置配置文件
                                    coroutineScope.launch {
                                        val acd = AppConfigCommon.getConfigData() ?: AppConfigData()
                                        acd.clientConfig.ipAddress = ipAddress
                                        acd.clientConfig.portAddress = portAddress
                                        acd.clientConfig.npcParam = npcParam
                                        AppConfigCommon.putConfig(acd)
                                        confirmClick(acd)
                                        showDialogState.value = false
                                    }
                                }
                            }
                        ) {
                            Text(text = "确定")
                        }
                    }
                }
            }
        }
    }
}


/**
 * 服务端 输入Ip，端口，npc参数
 */
@Composable
fun ServerInputServerPortDialogCompose(
    showDialogState: MutableState<Boolean>,
    confirmClick: (AppConfigData) -> Unit = {  }
) {
    if (showDialogState.value) {

        var portAddress by remember {
            mutableStateOf("")
        }

        LaunchedEffect(key1 = Unit) {
            val acd = AppConfigCommon.getConfigData() ?: AppConfigData()
            portAddress = acd.serverConfig.portAddress
        }

        val coroutineScope = rememberCoroutineScope()

        Dialog(
            onDismissRequest = {
                showDialogState.value = false
            }
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "请输入本地监听端口;")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Port:")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.width(80.dp),
                            maxLines = 1,
                            singleLine = true,
                            value = portAddress,
                            onValueChange = { portAddress = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            label = {
                                Text(text = "端口")
                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                                showDialogState.value = false
                            }
                        ) {
                            Text(text = "取消")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                                val portPattern = Pattern.compile("^\\d+\$")
                                if (portPattern.matcher(portAddress).matches()) {
                                    // 设置配置文件
                                    coroutineScope.launch {
                                        val acd = AppConfigCommon.getConfigData() ?: AppConfigData()
                                        acd.serverConfig.portAddress = portAddress
                                        AppConfigCommon.putConfig(acd)
                                        confirmClick(acd)
                                        showDialogState.value = false
                                    }
                                }
                            }
                        ) {
                            Text(text = "确定")
                        }
                    }
                }
            }
        }
    }
}