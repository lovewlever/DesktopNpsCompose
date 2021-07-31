package com.nps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nps.common.CallbackCommon
import com.nps.common.ServiceInfoLog
import com.nps.socket.ServiceSocket

@Composable
fun ServicePageCompose(
    modifier: Modifier = Modifier
) {

    val infoListState = remember {
        mutableStateListOf<Pair<ServiceInfoLog, String>>(ServiceInfoLog.LogInfo to "")
    }

    SideEffect {
        CallbackCommon.logCallback = { serviceInfoLog, string ->
            infoListState.add(serviceInfoLog to string)
        }
        ServiceSocket.startServer()
    }

    val lazyScrollState = rememberLazyListState()
    LaunchedEffect(key1 = infoListState.size) {
        lazyScrollState.scrollToItem(infoListState.size)
    }

    Column(
        modifier = modifier
    ) {
        LazyColumn(
            state = lazyScrollState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(infoListState) { item: Pair<ServiceInfoLog, String> ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .height(0.7.dp)
                    )
                    Text(
                        modifier = Modifier,
                        text = item.second,
                        fontSize = 13.sp,
                        color = if (item.first is ServiceInfoLog.LogError) Color.Red else Color.Black
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(0.7.dp)
                            .background(
                                Color(0xFFDEDEDE)
                            )
                    )
                }
            }
        }
    }
}