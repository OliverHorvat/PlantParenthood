import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundDark
import com.example.plantparenthood.ui.theme.backgroundLight

@Composable
fun LoadingScreen() {
    var backgroundColor = backgroundLight

    if(isSystemInDarkTheme()){
        backgroundColor = backgroundDark
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        LottieAnimation(modifier = Modifier.fillMaxSize(), composition = composition, speed = 3f)
    }
}
