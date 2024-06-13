import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.plantparenthood.Flower
import com.example.plantparenthood.ui.theme.backgroundGreen
import com.example.plantparenthood.ui.theme.buttonGreen
import com.example.plantparenthood.R

@Composable
fun GardenScreen(context: Context, navController: NavController, gardenViewModel: GardenViewModel) {
    val itemsState = remember { mutableStateOf<List<Flower>>(emptyList()) }

    LaunchedEffect(Unit) {
        val flowers = gardenViewModel.getFlowers(context)
        val items = flowers.map { flower ->
            Flower(name = flower.name, image = flower.image, type = flower.type, floweringTime = flower.floweringTime)
        }
        itemsState.value = items
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGreen)
        ) {
            Column {
                Button(
                    onClick = { navController.navigate("main_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, bottom = 16.dp)
                        .wrapContentWidth()
                ) {
                    Text("Return", fontSize = 16.sp)
                }
                RecyclerView(itemsState.value, navController, gardenViewModel)
            }
        }
    }
}

@Composable
fun RecyclerView(items: List<Flower>, navController: NavController, gardenViewModel: GardenViewModel) {
    LazyColumn {
        items(items) { item ->
            ListFlowerView(item = item, navController, gardenViewModel)
        }
    }
}

@Composable
fun ListFlowerView(item: Flower, navController: NavController, gardenViewModel: GardenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { gardenViewModel.goToFlower(item, navController) }
            .background(buttonGreen)
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
                painterResource(id = R.drawable.a)
            }

            Image(
                painter = painter,
                contentDescription = "Flower",
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}
