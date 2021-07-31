import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nps.common.AppPageNav
import com.nps.theme.DesktopNpsComposeTheme
import com.nps.ui.AppConfigSettingCompose
import com.nps.ui.ChooseServiceTypeCompose
import com.nps.ui.FileDirectoryOrListCompose
import com.nps.ui.ServicePageCompose

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    DesktopNpsComposeTheme {
        //TestSocketCompose()
        //FileDirectoryOrListCompose("/")
        var serviceTypeState: AppPageNav by remember {
            mutableStateOf(AppPageNav.TypeChooseSC)
        }

        Surface {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                ServicePageCompose(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )

                when (serviceTypeState) {
                    AppPageNav.TypeChooseSC -> {
                        ChooseServiceTypeCompose(
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            serviceTypeState = it
                        }
                    }
                    is AppPageNav.TypeServer -> {
                        ServicePageCompose()
                    }
                    is AppPageNav.TypeClient -> {
                        FileDirectoryOrListCompose(
                            modifier = Modifier.weight(1f)
                        )
                    }
                    is AppPageNav.TypeAppConfigSetting -> {
                        AppConfigSettingCompose(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {}
                }
            }
       }
    }
}