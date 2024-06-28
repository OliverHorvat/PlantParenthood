import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.plantparenthood.Plant
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundDark
import com.example.plantparenthood.ui.theme.backgroundLight
import com.example.plantparenthood.ui.theme.buttonDark
import com.example.plantparenthood.ui.theme.buttonLight

@Composable
fun GardenScreen(context: Context, navController: NavController, gardenViewModel: GardenViewModel) {
    var backgroundColor = backgroundLight
    var buttonColor = buttonLight

    if(isSystemInDarkTheme()){
        backgroundColor = backgroundDark
        buttonColor = buttonDark
    }

    val itemsState = remember { mutableStateOf<List<Plant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))

    LaunchedEffect(Unit) {
        val plants = gardenViewModel.getPlants(context)
        val items = plants.map { plant ->
            Plant(name = plant.name, image = plant.image, type = plant.type, documentId = plant.documentId)
        }
        itemsState.value = items
        isLoading = false
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column {
                if(isLoading){
                    LottieAnimation(modifier = Modifier.fillMaxSize(), composition = composition, speed = 3f)
                } else {
                    Button(
                        onClick = { navController.navigate("main_screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, bottom = 16.dp)
                            .wrapContentWidth()
                    ) {
                        Text("Return", fontSize = 16.sp, color = Color.White)
                    }
                    RecyclerView(itemsState.value, navController, gardenViewModel, buttonColor)
                }
            }
        }
    }
}

@Composable
fun RecyclerView(items: List<Plant>, navController: NavController, gardenViewModel: GardenViewModel, buttonColor: Color) {
    LazyColumn {
        items(items) { item ->
            ListPlantView(item = item, navController, gardenViewModel, buttonColor)
        }
    }
}

@Composable
fun ListPlantView(item: Plant, navController: NavController, gardenViewModel: GardenViewModel, buttonColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { gardenViewModel.goToPlant(item, navController) }
            .background(buttonColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = if (item.image != "") {
                rememberImagePainter(data = item.image)
            } else {
                painterResource(id = R.drawable.plant)
            }

            Image(
                painter = painter,
                contentDescription = "Plant",
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}
