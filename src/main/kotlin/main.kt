import androidx.compose.desktop.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.nps.common.AppPageNav
import com.nps.theme.DesktopNpsComposeTheme
import com.nps.ui.*
import kotlinx.coroutines.launch

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var serviceTypeState: AppPageNav by remember {
        mutableStateOf(AppPageNav.TypeAppConfigSetting)
    }

    DesktopNpsComposeTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(40.dp),
                    backgroundColor = Color.White,
                    title = {},
                    navigationIcon = {
                        Icon(
                            painter = svgResource("img/ic_more.svg"),
                            contentDescription = "er",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp)
                                .clickable {
                                    coroutineScope.launch { scaffoldState.drawerState.open() }
                                },
                            tint = Color.Black
                        )
                    }
                )
            },
            drawerContent = {
                HomeDrawerCompose(
                    openPageClick = {
                        serviceTypeState = it
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                    }
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
        ) {

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
    }
}