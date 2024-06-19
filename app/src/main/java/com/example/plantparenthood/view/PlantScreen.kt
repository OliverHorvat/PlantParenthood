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
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


@Composable
fun PlantScreen(context: Context, flower: Flower, navController: NavController, plantViewModel: PlantViewModel) {
    var daysBetweenWatering by remember { mutableIntStateOf(0) }
    val overdue = remember { mutableStateOf(false) }
    var days = remember { mutableStateOf(0) }
    var hours = remember { mutableStateOf(0) }
    var minutes = remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var daysText = remember { mutableStateOf("") }
    var minutesText = remember { mutableStateOf("") }
    var hoursText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        daysBetweenWatering = plantViewModel.fetchDaysBetweenWatering(context, flower.type)
        isLoading = false
        while (true) {
            val (isOverdue, overdueMinutes) = plantViewModel.calculateWateringTime(
                flower.floweringTime,
                daysBetweenWatering
            )
            overdue.value = isOverdue
            days.value = TimeUnit.MINUTES.toDays(overdueMinutes.toLong()).toInt()
            hours.value = TimeUnit.MINUTES.toHours(overdueMinutes.toLong()).toInt() % 24
            minutes.value = overdueMinutes % 60

            daysText.value = if (days.value == 1) {
                "${days.value} day, "
            } else {
                "${days.value} days, "
            }

            hoursText.value = if (hours.value == 1) {
                "${hours.value} hour, "
            } else {
                "${hours.value} hours, "
            }

            minutesText.value = if (minutes.value == 1) {
                "${minutes.value} minute"
            } else {
                "${minutes.value} minutes"
            }
            delay(60000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGreen)
    ) {
        if(!isLoading){
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
                        onClick = { navController.navigateUp() },
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
                    text = if (overdue.value) "Watering is overdue by:" else "Watering is due in:",
                    textAlign = TextAlign.Center,
                    color = if (overdue.value) Color.Red else Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${daysText.value}${hoursText.value}${minutesText.value}",
                    textAlign = TextAlign.Center,
                    color = if (overdue.value) Color.Red else Color.Black,
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
}
