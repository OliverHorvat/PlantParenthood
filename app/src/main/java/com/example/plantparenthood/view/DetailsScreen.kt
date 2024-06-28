import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.plantparenthood.PlantType
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundDark
import com.example.plantparenthood.ui.theme.backgroundLight
import com.example.plantparenthood.ui.theme.buttonDark
import com.example.plantparenthood.ui.theme.buttonLight

@Composable
fun DetailsScreen(context:Context, type: String, navController: NavController, detailsViewModel: DetailsViewModel) {
    var backgroundColor = backgroundLight
    var buttonColor = buttonLight
    var textColor = Color.Black

    if(isSystemInDarkTheme()){
        backgroundColor = backgroundDark
        buttonColor = buttonDark
        textColor = Color.White
    }

    var typeDetails by remember { mutableStateOf(PlantType()) }
    var isLoading by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))

    LaunchedEffect(Unit) {
        typeDetails = detailsViewModel.getTypeDetails(context, type) ?: PlantType()
        isLoading = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if(isLoading){
                LottieAnimation(modifier = Modifier.fillMaxSize(), composition = composition, speed = 3f)
            } else {
                Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .wrapContentWidth()
                ) {
                    Text("Return", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = type,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    rememberImagePainter(data = typeDetails.image),
                    contentDescription = "Plant",
                    modifier = Modifier
                        .size(260.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = typeDetails.description,
                    textAlign = TextAlign.Left,
                    color = textColor,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp)
                    )
            }
        }
    }
}