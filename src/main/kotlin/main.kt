import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.nps.common.NPCCommon
import com.nps.ui.TestSocketCompose

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    SideEffect {
        NPCCommon.startNpcClient("-server=8.140.170.239:8024 -vkey=nilgerw43tew0c8n -type=tcp -password=remote -target=127.0.0.1:2120 127.0.0.1:2121")
    }

    MaterialTheme {
        TestSocketCompose()
        //FileDirectoryOrListCompose("/")
        //ChooseServiceTypeCompose()
    }
}