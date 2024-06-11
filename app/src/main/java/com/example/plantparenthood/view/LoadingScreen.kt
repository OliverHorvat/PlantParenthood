import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plantparenthood.ui.theme.backgroundGreen

@Composable
fun LoadingScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 48.dp)
        ) {
            Spacer(modifier = Modifier.height(174.dp))
            Text(
                text = "Plant Parenthood",
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 44.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
