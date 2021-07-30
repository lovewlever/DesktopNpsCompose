package com.nps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nps.common.SocketInteractiveKey
import com.nps.model.InteractiveData
import com.nps.socket.ClientSocket
import java.io.File
import javax.swing.plaf.IconUIResource

@Composable
fun FileDirectoryOrListCompose(
    rootDirectory: String = "D:\\"
) {

    val filesListState = remember {
        mutableStateOf<List<InteractiveData>>(listOf())
    }

    SideEffect {
        ClientSocket.directoryListCallback = {
            filesListState.value = it
        }
        ClientSocket.connect()
    }

    var rootDirectoryState by remember {
        mutableStateOf(rootDirectory)
    }

    DisposableEffect(key1 = rootDirectoryState) {
        ClientSocket.sentMsg(SocketInteractiveKey.GetDirectory, rootDirectoryState, "")
        onDispose {

        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Image(
                    painter = svgResource("img/back.svg"),
                    contentDescription = "",
                    modifier = Modifier
                        .rotate(180f)
                        .clickable {
                            File(rootDirectoryState).parent?.let {
                                rootDirectoryState = it
                            }
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {

                }) {
                    Text(text = "下载本页")
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filesListState.value) { file ->
                if (file.isDirectory) {
                    FileDirectoryCompose(
                        interactiveData = file,
                        directoryClick = {
                            rootDirectoryState = file.filePath
                        }
                    )
                } else {
                    FileCompose(
                        interactiveData = file
                    )
                }
            }
        }
    }
}

@Composable
private fun FileDirectoryCompose(
    interactiveData: InteractiveData,
    directoryClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(45.dp)
            .clickable {
                directoryClick()
            }
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = svgResource("img/directory.svg"),
            contentDescription = "",
            tint = Color(0xFF3788FC),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = interactiveData.fileName)
    }
}

@Composable
private fun FileCompose(
    interactiveData: InteractiveData,
    downloadClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(45.dp)
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = svgResource("img/file.svg"),
            contentDescription = "",
            tint = Color(0xFF3788FC),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = interactiveData.fileName)
            Text(
                text = interactiveData.filePath,
                fontSize = 12.sp,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            downloadClick()
        }) {
            Text(text = "下载")
        }
    }
}