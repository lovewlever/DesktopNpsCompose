import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nps.common.ServiceType
import com.nps.ui.ChooseServiceTypeCompose
import com.nps.ui.TestSocketCompose

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    MaterialTheme {
        //TestSocketCompose()
        //FileDirectoryOrListCompose("/")
        var serviceTypeState: ServiceType by remember {
            mutableStateOf(ServiceType.TypeChooseSC)
        }
        when (serviceTypeState) {
            ServiceType.TypeChooseSC -> {
                ChooseServiceTypeCompose {
                    serviceTypeState = it
                }
            }
            is ServiceType.TypeServer -> {
                TestSocketCompose()
            }
            is ServiceType.TypeClient -> {
                TestSocketCompose()
            }
            else -> {}
        }

    }
}