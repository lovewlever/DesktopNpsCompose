import androidx.compose.desktop.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nps.common.AppPageNav
import com.nps.common.NPCCommon
import com.nps.socket.ClientSocket
import com.nps.theme.DesktopNpsComposeTheme
import com.nps.ui.*
import kotlinx.coroutines.launch


fun logcatMain() = Window(
    size = IntSize(557, 364)
) {
    DesktopNpsComposeTheme {
        LogcatCompose()
    }
}


@OptIn(ExperimentalMaterialApi::class)
fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
    onDismissRequest = {
        NPCCommon.destroyExec()
    }
) {
    val scaffoldState = rememberScaffoldState()
    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var serviceTypeState: AppPageNav by remember {
        mutableStateOf(AppPageNav.TypeAppConfigSetting)
    }
    logcatMain()
    DesktopNpsComposeTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(40.dp),
                    backgroundColor = Color.White,
                    title = {
                            Row {
                                Text(
                                    text = "服务端状态：",
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "客户端状态：",
                                    fontSize = 12.sp
                                )
                            }
                    },
                    navigationIcon = {
                        Icon(
                            painter = svgResource("img/ic_more.svg"),
                            contentDescription = "er",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        if (bottomDrawerState.isOpen)
                                            bottomDrawerState.close()
                                        else
                                            bottomDrawerState.open()
                                    }
                                },
                            tint = Color.Black
                        )
                    }
                )
            }
        ) {

            BottomDrawer(
                gesturesEnabled = true,
                drawerState = bottomDrawerState,
                drawerContent = {
                    HomeDrawerCompose(
                        openPageClick = {
                            serviceTypeState = it
                            coroutineScope.launch { bottomDrawerState.close() }
                        },
                        closeClick = { coroutineScope.launch { bottomDrawerState.close() } }
                    )
                },
                content = {
                    Surface {
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            when (serviceTypeState) {
                                is AppPageNav.TypeServer -> {
                                    ServicePageCompose(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                is AppPageNav.TypeClient -> {
                                    FileDirectoryOrListCompose(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                is AppPageNav.TypeAppConfigSetting -> {
                                    AppConfigSettingCompose(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
            )


            /*ServicePageCompose(
                modifier = Modifier.fillMaxSize()
            )*/
        }
    }
}