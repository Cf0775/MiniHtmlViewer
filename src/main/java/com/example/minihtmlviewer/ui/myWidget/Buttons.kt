import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//模仿安卓2.x的按钮
@Composable
fun RetroButton(
    text: String, // 按钮上的文字
    onClick: () -> Unit, // 点击事件
    modifier: Modifier = Modifier, // 允许外部传递 Modifier
    backgroundColor: Color = Color.DarkGray, // 可选背景色，默认是深灰色
    textColor: Color = Color.White // 文字颜色
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(8.dp)
            .background(backgroundColor, shape = RoundedCornerShape(4.dp)) // 经典深色背景
            .border(2.dp, Color.White, shape = RoundedCornerShape(4.dp)) // 模拟 3D 效果
            .clickable { onClick() } // 让整个 Box 变成可点击
            .padding(12.dp)
    ) {
        Text(text, color = textColor)
    }
}
