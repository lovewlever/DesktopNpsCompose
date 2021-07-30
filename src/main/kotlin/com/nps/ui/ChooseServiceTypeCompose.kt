package com.nps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nps.common.NPCCommon
import com.nps.common.ServiceType

/**
 * 启动时选择服务方式
 */
@Composable
fun ChooseServiceTypeCompose(
    typeClick: (ServiceType) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row{
            Button(
                onClick = {
                    typeClick(ServiceType.TypeServer)
                }
            ) {
                Text(
                    text = "作为服务端启动"
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Button(
                onClick = {
                    NPCCommon.startNpcClient("-server=8.140.170.239:8024 -vkey=nilgerw43tew0c8n -type=tcp -password=remote -target=127.0.0.1:8025")
                    typeClick(ServiceType.TypeClient)
                }
            ) {
                Text(
                    text = "作为客户端启动"
                )
            }
        }
    }

}