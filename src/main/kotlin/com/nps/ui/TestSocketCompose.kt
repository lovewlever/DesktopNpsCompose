package com.nps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nps.socket.ClientSocket
import com.nps.socket.ServiceSocket

/**
 * 测试Socket
 */
@Composable
fun TestSocketCompose() {

    val socketClient by remember {
        mutableStateOf(ClientSocket())
    }

    val socketServer by remember {
        mutableStateOf(ServiceSocket())
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row {
                Button(
                    onClick = {
                        socketServer.connect()
                    }
                ) {
                    Text(text = "开启服务")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        socketClient.connect()
                    }
                ) {
                    Text(text = "开启客户端")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            var sendText by remember {
                mutableStateOf("")
            }
            Row {
                TextField(
                    value = sendText,
                    onValueChange = {
                        sendText = it
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        socketClient.sentMsg(
                            "C:\\Users\\53273\\Downloads\\Broadcom_bluetooth_12004900w8.zip",
                            "C:\\Users\\AOC\\Desktop\\release.zip"
                        )
                    }
                ) {
                    Text(text = "发送")
                }
            }
        }
    }
}