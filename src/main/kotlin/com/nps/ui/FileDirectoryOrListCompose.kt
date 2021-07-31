package com.nps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.nps.common.CallbackCommon
import com.nps.common.SocketInteractiveKey
import com.nps.common.suffixText
import com.nps.model.SocketFileData
import com.nps.model.SocketInteractiveData
import com.nps.socket.ClientSocket
import java.io.File

/**
 * 文件列表
 */
@Composable
fun FileDirectoryOrListCompose(
    modifier: Modifier = Modifier,
    rootDirectory: String = "C:\\"
) {

    val rootDirectoryState = remember {
        mutableStateOf(rootDirectory)
    }

    Column(
        modifier = modifier
    ) {
        // 顶部操作栏
        TopActionBarCompose(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 8.dp, end = 8.dp),
            rootDirectoryState = rootDirectoryState
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 左侧操作栏
            LeftActionBarCompose(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp),
                rootDirectoryState = rootDirectoryState
            )

            Spacer(
                modifier = Modifier
                    .width(0.7.dp)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFFDEDEDE)
                    )
            )
            // 右侧列表
            RightLazyColumnCompose(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                rootDirectoryState = rootDirectoryState
            )
        }
    }
}

/**
 * 顶部操作栏
 */
@Composable
private fun TopActionBarCompose(
    modifier: Modifier = Modifier,
    rootDirectoryState: MutableState<String>
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = svgResource("img/back.svg"),
                contentDescription = "",
                modifier = Modifier
                    .rotate(180f)
                    .clickable {
                        File(rootDirectoryState.value).parent?.let {
                            rootDirectoryState.value = it
                            ClientSocket.sentMsg(SocketInteractiveKey.GetDirectory, it, "")
                        }
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(28.dp)
                    .requiredWidth(80.dp),
                onClick = {

                }
            ) {
                Text(text = "下载本页")
            }
        }
    }
}

/**
 * 左侧操作栏
 */
@Composable
private fun LeftActionBarCompose(
    modifier: Modifier = Modifier,
    rootDirectoryState: MutableState<String>
) {

    var rootDirStrPath by remember {
        mutableStateOf("")
    }

    var progressSchedule by remember {
        mutableStateOf(0F)
    }

    SideEffect {
        CallbackCommon.fileDownloadScheduleCallback = { available: Long, curSchedule: Long ->
            progressSchedule = curSchedule / available.toFloat()
        }
    }

    Column(
        modifier = modifier
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = progressSchedule
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = rootDirStrPath,
            onValueChange = { rootDirStrPath = it },
            label = {
                Text(text = "根路径")
            }
        )
        OutlinedButton(
            onClick = {
                rootDirectoryState.value = rootDirStrPath
            }
        ) {
            Text(text = "访问")
        }

    }
}

/**
 * 右侧文件列表
 */
@Composable
private fun RightLazyColumnCompose(
    modifier: Modifier = Modifier,
    rootDirectoryState: MutableState<String>
) {

    val filesListState = remember {
        mutableStateOf<List<SocketFileData>>(listOf())
    }

    LaunchedEffect(key1 = Unit) {
        CallbackCommon.directoryListCallback = {
            filesListState.value = it
        }
        ClientSocket.connect(rootDirectoryState.value)
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(filesListState.value) { file ->
            if (file.isDirectory) {
                FileDirectoryCompose(
                    interactiveData = file,
                    directoryClick = {
                        rootDirectoryState.value = file.filePath
                        ClientSocket.sentMsg(SocketInteractiveKey.GetDirectory, file.filePath, "")
                    }
                )
            } else {
                FileCompose(
                    interactiveData = file,
                    downloadClick = {
                        file.fileName + file.filePath.subSequence(
                            file.filePath.lastIndexOf("."),
                            file.filePath.length
                        )
                        ClientSocket.sentMsg(
                            SocketInteractiveKey.DownloadFile,
                            file.filePath,
                            "C:\\Users\\AOC\\Desktop\\${file.fileName}.${file.filePath.suffixText(".")}",
                            fileSize = file.fileSize
                        )
                    }
                )
            }
        }
    }
}

/**
 * 文件夹列表 Item
 */
@Composable
private fun FileDirectoryCompose(
    interactiveData: SocketFileData,
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

/**
 * 文件列表 Item
 */
@Composable
private fun FileCompose(
    interactiveData: SocketFileData,
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
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = interactiveData.fileName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = interactiveData.filePath,
                fontSize = 12.sp,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .height(28.dp)
                .requiredWidth(60.dp),
            onClick = {
                downloadClick()
            }
        ) {
            Text(
                text = "下载"
            )
        }
    }
}