import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.plantparenthood.ui.theme.lightRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun PlantScreen(context: Context, plantId: String, navController: NavController, plantViewModel: PlantViewModel) {
    var backgroundColor = backgroundLight
    var buttonColor = buttonLight
    var textColor = Color.Black
    var overdueColor = Color.Red
    if(isSystemInDarkTheme()){
        backgroundColor = backgroundDark
        buttonColor = buttonDark
        textColor = Color.White
        overdueColor = lightRed
    }

    var refreshTrigger by remember { mutableStateOf(0) }
    var daysBetweenWatering by remember { mutableIntStateOf(0) }
    val overdue = remember { mutableStateOf(false) }
    val days = remember { mutableStateOf(0) }
    val hours = remember { mutableStateOf(0) }
    val minutes = remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    val daysText = remember { mutableStateOf("") }
    val minutesText = remember { mutableStateOf("") }
    val hoursText = remember { mutableStateOf("") }
    var plant by remember { mutableStateOf(Plant()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showWaterDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))

    LaunchedEffect(refreshTrigger) {
        plant = plantViewModel.getPlantById(context, plantId) ?: Plant()
        daysBetweenWatering = plantViewModel.fetchDaysBetweenWatering(context, plant.type)
        isLoading = false
        while (true) {
            val (isOverdue, overdueMinutes) = plantViewModel.calculateWateringTime(
                plant.wateringTime,
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
            plantViewModel.type = plant.type
            delay(60000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if(isLoading){
            LottieAnimation(modifier = Modifier.fillMaxSize(), composition = composition, speed = 3f)
        } else {
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
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp)
                    ) {
                        Text("Return", fontSize = 16.sp, color = Color.White)
                    }

                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp)
                    ) {
                        Text("Delete", fontSize = 16.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${plant.type}:",
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontSize = 32.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = plant.name,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor,
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val painter = if (plant.image != "") {
                    rememberImagePainter(data = plant.image)
                } else {
                    painterResource(id = R.drawable.plant)
                }

                Image(
                    painter = painter,
                    contentDescription = "Plant",
                    modifier = Modifier
                        .size(260.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (overdue.value) "Watering is overdue by:" else "Watering is due in:",
                    textAlign = TextAlign.Center,
                    color = if (overdue.value) overdueColor else textColor,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${daysText.value}${hoursText.value}${minutesText.value}",
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (overdue.value) overdueColor else textColor,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showWaterDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Text("Water Me", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("edit_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Text("Edit Me", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("details_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Text("Type Details", fontSize = 16.sp, color = Color.White)
                }
            }
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    containerColor = backgroundColor,
                    title = { Text(
                        text = "Are you sure that you want to delete\n''${plant.name}''?",
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = textColor
                    )},
                    confirmButton = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        plantViewModel.deletePlantById(context, plantId, plant.image)
                                        showDeleteDialog = false
                                        navController.navigate("garden_screen")
                                    } },
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text("Yes", fontSize = 18.sp, color = Color.White)
                            }

                            Button(
                                onClick = { showDeleteDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text("No", fontSize = 18.sp, color = Color.White)
                            }
                        }
                    }
                )
            }
            if (showWaterDialog) {
                AlertDialog(
                    onDismissRequest = { showWaterDialog = false },
                    containerColor = backgroundColor,
                    title = { Text(
                        text = "Confirm watering\n''${plant.name}''",
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = textColor
                    )},
                    confirmButton = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        plantViewModel.updateWateringTime(context, plantId)
                                        showWaterDialog = false
                                        refreshTrigger++
                                    }},
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text("Confirm", fontSize = 18.sp, color = Color.White)
                            }

                            Button(
                                onClick = { showWaterDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                Text("Dismiss", fontSize = 18.sp, color = Color.White)
                            }
                        }
                    }
                )
            }
        }
    }
}
