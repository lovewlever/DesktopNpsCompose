import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nps.ui.FileDirectoryOrListCompose
import java.io.File
import javax.imageio.ImageIO

fun main() = Window(
    //icon = ImageIO.read(File("nps.png"))
) {
    MaterialTheme {
        FileDirectoryOrListCompose("D:\\")
    }
}