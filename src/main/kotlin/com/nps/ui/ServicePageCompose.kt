package com.nps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nps.common.ServiceInfoLog
import com.nps.socket.ClientSocket
import com.nps.socket.ServiceSocket

@Composable
fun ServicePageCompose() {

    val infoListState = remember {
        mutableStateListOf<Pair<ServiceInfoLog, String>>(ServiceInfoLog.LogInfo to "")
    }
    SideEffect {
        ServiceSocket.connect(callback = { serviceInfoLog, string ->
            infoListState.add(serviceInfoLog to string)
        })
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(infoListState) { item: Pair<ServiceInfoLog, String> ->
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = item.second,
                        textAlign = TextAlign.Center,
                        color = if (item.first is ServiceInfoLog.LogError) Color.Red else Color.Black
                    )
                }
            }
        }
    }
}