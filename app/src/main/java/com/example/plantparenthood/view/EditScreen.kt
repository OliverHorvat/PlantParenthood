import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.plantparenthood.Plant
import com.example.plantparenthood.R
import com.google.firebase.Timestamp
import java.util.Calendar
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import java.io.File
import java.util.Objects
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.plantparenthood.ui.theme.backgroundDark
import com.example.plantparenthood.ui.theme.backgroundLight
import com.example.plantparenthood.ui.theme.buttonDark
import com.example.plantparenthood.ui.theme.buttonLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(context: Context, plantId: String, navController: NavController, editViewModel: EditViewModel) {
    var backgroundColor = backgroundLight
    var buttonColor = buttonLight
    var textColor = Color.Black

    if(isSystemInDarkTheme()){
        backgroundColor = backgroundDark
        buttonColor = buttonDark
        textColor = Color.White
    }

    val name = remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }
    var ownerId by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var plantTypes by remember { mutableStateOf<List<String>>(emptyList()) }
    var imageCaptured by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var plant by remember { mutableStateOf(Plant()) }
    var isLoading by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))

    LaunchedEffect(Unit) {
        plantTypes = editViewModel.getPlantTypes()
        if (plantId != "") {
            plant = editViewModel.getPlantById(context, plantId) ?: Plant()
            name.value = plant.name
            type = plant.type
            ownerId = plant.ownerId
            plant.documentId = plantId
            val calendar = Calendar.getInstance()
            calendar.time = plant.wateringTime.toDate()
            year = calendar.get(Calendar.YEAR).toString()
            month = (calendar.get(Calendar.MONTH) + 1).toString()
            day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
            minute = calendar.get(Calendar.MINUTE).toString()

            if (plant.image != "") {
                imageCaptured = true
            }
        }
        isLoading = false
    }

    val file = File.createTempFile(
        "plant",
        ".jpg",
        context.externalCacheDir
    )
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                capturedImageUri = uri
                imageCaptured = true
            }
        }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
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
                Button(
                    onClick = { navController.navigateUp() },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .wrapContentWidth()
                ) {
                    Text("Return", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (imageCaptured) {
                    val painter = if (capturedImageUri == Uri.EMPTY) {
                        rememberImagePainter(data = plant.image)
                    } else {
                        rememberImagePainter(capturedImageUri)
                    }
                    Image(
                        painter = painter,
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .size(240.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.plant),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(240.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Plant's Name", color = textColor) },
                    textStyle = TextStyle(color = textColor),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textColor,
                        unfocusedBorderColor = textColor,
                        cursorColor = textColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Last Watering Time:",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = year,
                        label = { Text("YYYY", color = textColor, style = TextStyle(fontSize = 10.sp)) },
                        onValueChange = { year = it.filter { char -> char.isDigit() }.take(4) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = textColor),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = textColor,
                            cursorColor = textColor
                        ),
                        modifier = Modifier
                            .weight(1.75f)
                            .padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    OutlinedTextField(
                        value = month,
                        label = { Text(text = "MM", color = textColor, style = TextStyle(fontSize = 10.sp)) },
                        onValueChange = { month = it.filter { char -> char.isDigit() }.take(2) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = textColor),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = textColor,
                            cursorColor = textColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    OutlinedTextField(
                        value = day,
                        label = { Text("DD", color = textColor, style = TextStyle(fontSize = 10.sp)) },
                        onValueChange = { day = it.filter { char -> char.isDigit() }.take(2) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = textColor),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = textColor,
                            cursorColor = textColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    OutlinedTextField(
                        value = hour,
                        label = { Text("HH", color = textColor, style = TextStyle(fontSize = 10.sp)) },
                        onValueChange = { hour = it.filter { char -> char.isDigit() }.take(2) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = textColor),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = textColor,
                            cursorColor = textColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    OutlinedTextField(
                        value = minute,
                        label = { Text("mm", color = textColor, style = TextStyle(fontSize = 10.sp)) },
                        onValueChange = { minute = it.filter { char -> char.isDigit() }.take(2) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = textColor),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = textColor,
                            cursorColor = textColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Text("Take a Picture", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Text("Select Plant Type", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.value == "") {
                            Toast.makeText(context, "Please input name", Toast.LENGTH_SHORT).show()
                        } else if (type == "") {
                            Toast.makeText(context, "Please select type", Toast.LENGTH_SHORT).show()
                        } else if (year == "" || month == "" || day == "" || hour == "" || minute == "") {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                                .show()
                        } else if (year.toInt() > 1 && month.toInt() > 0 && day.toInt() > 0 && hour.toInt() > -1 && minute.toInt() > -1) {
                            Toast.makeText(
                                context,
                                "Saving in progress, please wait",
                                Toast.LENGTH_SHORT
                            ).show()
                            val calendar = Calendar.getInstance()
                            calendar.set(
                                year.toInt(),
                                month.toInt() - 1,
                                day.toInt(),
                                hour.toInt(),
                                minute.toInt(),
                                0
                            )
                            val wateringTime = Timestamp(calendar.time)
                            val newPlant = Plant(
                                name = name.value,
                                wateringTime = wateringTime,
                                type = type,
                                ownerId = ownerId,
                                documentId = plant.documentId
                            )
                            if (plant.documentId != "") {

                                if (capturedImageUri == Uri.EMPTY) {
                                    newPlant.image = plant.image
                                    editViewModel.editPlant(context, newPlant)
                                    navController.navigate("plant_screen")
                                } else {
                                    editViewModel.uploadImageToFirebase(capturedImageUri,
                                        onSuccess = { downloadUrl ->
                                            newPlant.image = downloadUrl
                                            editViewModel.editPlant(context, newPlant)
                                            if (plant.image != "") {
                                                editViewModel.deleteImageFromFirebase(plant.image)
                                            }
                                            navController.navigate("plant_screen")
                                        },
                                        onFailure = {
                                            Toast.makeText(
                                                context,
                                                "Image has not been saved",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            } else {
                                editViewModel.uploadImageToFirebase(capturedImageUri,
                                    onSuccess = { downloadUrl ->
                                        newPlant.image = downloadUrl
                                        editViewModel.addPlant(context, newPlant)
                                    },
                                    onFailure = { filler ->
                                        newPlant.image = filler
                                        editViewModel.addPlant(context, newPlant)
                                    }
                                )
                            }
                        } else {
                            Toast.makeText(context, "Please input valid date", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Text("Save", fontSize = 16.sp, color = Color.White)
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Select Plant Type:", color = textColor) },
                    containerColor = backgroundColor,
                    text = {
                        LazyColumn {
                            items(plantTypes) { plantType ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {
                                            type = plantType
                                            showDialog = false
                                        }
                                        .background(
                                            color = buttonColor,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = plantType,
                                        color = Color.White,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text("Close", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}