import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.plantparenthood.Flower
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundGreen
import com.example.plantparenthood.ui.theme.buttonGreen


@Composable
fun PlantScreen(context: Context, flower: Flower, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("garden_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                ) {
                    Text("Return", fontSize = 16.sp)
                }

                Button(
                    onClick = { /* Handle delete action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                ) {
                    Text("Delete", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${flower.type} ${flower.name}",
                color = Color.Black,
                fontSize = 44.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val painter = if (flower.image != "") {
                rememberImagePainter(data = flower.image)
            } else {
                painterResource(id = R.drawable.a)
            }

            Image(
                painter = painter,
                contentDescription = "Plant",
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Watering is overdue by:\n3 days\n2 hours",
                textAlign = TextAlign.Center,
                color = Color.Red,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle water plant action */ },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Water Me", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle edit plant action */ },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Edit Me", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle details action */ },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Type Details", fontSize = 16.sp)
            }
        }
    }
}
