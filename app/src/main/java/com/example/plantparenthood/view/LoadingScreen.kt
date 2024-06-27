import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundGreen

@Composable
fun LoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
            .padding(16.dp)
    ) {
        LottieAnimation(modifier = Modifier.fillMaxSize(), composition = composition, speed = 3f)
    }
}
