package com.nps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 启动时选择服务方式
 */
@Composable
fun ChooseServiceTypeCompose() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row{
            Button(
                onClick = {

                }
            ) {
                Text(
                    text = "作为服务端启动"
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Button(
                onClick = {

                }
            ) {
                Text(
                    text = "作为客户端启动"
                )
            }
        }
    }

}