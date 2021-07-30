import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.nps.common.NPCCommon
import com.nps.ui.TestSocketCompose

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    MaterialTheme {
        TestSocketCompose()
        //FileDirectoryOrListCompose("/")
        //ChooseServiceTypeCompose()
    }
}