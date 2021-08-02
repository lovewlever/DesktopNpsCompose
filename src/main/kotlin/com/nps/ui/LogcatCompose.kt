package com.nps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nps.common.AppConfigCommon
import com.nps.common.AppLogType
import com.nps.common.CallbackCommon
import com.nps.socket.ServiceSocket
import kotlinx.coroutines.launch

@Composable
fun LogcatCompose(
    modifier: Modifier = Modifier
) {
    val infoListState = remember {
        mutableStateListOf<Pair<AppLogType, String>>(AppLogType.LogInfo to "")
    }
    SideEffect {
        CallbackCommon.logCallback = { serviceInfoLog, string ->
            infoListState.add(serviceInfoLog to string)
        }
        CallbackCommon.execProcessCallback = { serviceInfoLog, string ->
            infoListState.add(serviceInfoLog to string)
        }
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
            items(infoListState) { item: Pair<AppLogType, String> ->
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
                        color = if (item.first is AppLogType.LogError) Color.Red else Color.Black
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