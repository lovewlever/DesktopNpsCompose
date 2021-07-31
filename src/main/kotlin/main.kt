import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nps.common.ServiceType
import com.nps.ui.ChooseServiceTypeCompose
import com.nps.ui.FileDirectoryOrListCompose
import com.nps.ui.ServicePageCompose

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    MaterialTheme {
        //TestSocketCompose()
        //FileDirectoryOrListCompose("/")
        var serviceTypeState: ServiceType by remember {
            mutableStateOf(ServiceType.TypeChooseSC)
        }
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            ServicePageCompose(
                modifier = Modifier.weight(1f).fillMaxHeight()
            )

            when (serviceTypeState) {
                ServiceType.TypeChooseSC -> {
                    ChooseServiceTypeCompose(
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ) {
                        serviceTypeState = it
                    }
                }
                is ServiceType.TypeServer -> {
                    ServicePageCompose()
                }
                is ServiceType.TypeClient -> {
                    FileDirectoryOrListCompose(
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {}
            }
        }



    }
}