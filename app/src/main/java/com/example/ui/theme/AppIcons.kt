package com.example.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcons {
    private inline fun buildIcon(name: String, crossinline block: PathBuilder.() -> Unit): ImageVector {
        return ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                block()
            }
        }.build()
    }

    val Psychology: ImageVector by lazy {
        buildIcon("Psychology") {
            moveTo(12f, 3f)
            curveTo(7.03f, 3f, 3f, 7.03f, 3f, 12f)
            curveTo(3f, 16.97f, 7.03f, 21f, 12f, 21f)
            curveTo(16.97f, 21f, 21f, 16.97f, 21f, 12f)
            curveTo(21f, 7.03f, 16.97f, 3f, 12f, 3f)
            close()
            moveTo(12f, 7f)
            curveTo(13.66f, 7f, 15f, 8.34f, 15f, 10f)
            curveTo(15f, 11.66f, 13.66f, 13f, 12f, 13f)
            curveTo(10.34f, 13f, 9f, 11.66f, 9f, 10f)
            curveTo(9f, 8.34f, 10.34f, 7f, 12f, 7f)
            close()
        }
    }

    val AccessTime: ImageVector = Icons.Default.DateRange
    val WbSunny: ImageVector = Icons.Default.Star
    val Build: ImageVector = Icons.Default.Settings
    val History: ImageVector = Icons.Default.DateRange
    val DeleteSweep: ImageVector = Icons.Default.Delete
    val Security: ImageVector = Icons.Default.Lock
    val Mic: ImageVector = Icons.Default.Phone
    val TouchApp: ImageVector = Icons.Default.ThumbUp
    val Visibility: ImageVector = Icons.Default.Search
    val Headphones: ImageVector = Icons.Default.Notifications
    val Pause: ImageVector = Icons.Default.Close
    val Lightbulb: ImageVector = Icons.Default.Star
    val AutoAwesome: ImageVector = Icons.Default.Star
    val CameraFront: ImageVector = Icons.Default.Person
    val ScreenRotation: ImageVector = Icons.Default.Refresh
    val VolumeUp: ImageVector = Icons.Default.Notifications
    val StopCircle: ImageVector = Icons.Default.Close
    val PlayCircle: ImageVector = Icons.Default.PlayArrow
    val GraphicEq: ImageVector = Icons.Default.Menu
    val Speed: ImageVector = Icons.Default.Star
    val WaterDrop: ImageVector = Icons.Default.Favorite
    val Info: ImageVector = Icons.Default.Info
    val Settings: ImageVector = Icons.Default.Settings
    val CloseIcon: ImageVector = Icons.Default.Close
    val SendIcon: ImageVector = Icons.Default.Send
    val RefreshIcon: ImageVector = Icons.Default.Refresh
    val RecordVoiceOver: ImageVector = Icons.Default.Phone
    val VisibilityOff: ImageVector = Icons.Default.Close
    val WarningAmber: ImageVector = Icons.Default.Info
    val Hub: ImageVector = Icons.Default.Menu
    val Mood: ImageVector = Icons.Default.Star
    val Image: ImageVector = Icons.Default.Star
    val CheckCircle: ImageVector = Icons.Default.ThumbUp
    val AccountTree: ImageVector = Icons.Default.Menu
    val Keyboard: ImageVector = Icons.Default.Phone
    val BatteryChargingFull: ImageVector = Icons.Default.Favorite
    val Timeline: ImageVector = Icons.Default.DateRange
    val Waves: ImageVector = Icons.Default.PlayArrow
}
