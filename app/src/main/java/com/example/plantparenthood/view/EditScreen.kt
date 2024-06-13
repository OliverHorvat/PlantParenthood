import EditViewModel
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
import com.example.plantparenthood.Flower
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundGreen
import com.example.plantparenthood.ui.theme.buttonGreen
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.TimeZone
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import java.io.File
import java.util.Objects
import coil.compose.rememberImagePainter
@Composable
fun EditScreen(context: Context, navController: NavController, editViewModel: EditViewModel) {
    val name = remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var plantTypes by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            plantTypes = editViewModel.getPlantTypes()
        }
    }
    val file = File.createTempFile(
        "flower",
        ".jpg",
        context.externalCacheDir
    )
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var imageCaptured by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

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
            .background(backgroundGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("main_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .wrapContentWidth()
            ) {
                Text("Return", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (imageCaptured) {
                Image(
                    painter = rememberImagePainter(capturedImageUri),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.a),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = name.value,

                onValueChange = { name.value = it },
                label = { Text("Plant's Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Last Watering Time:",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = year,
                    label = { Text("YYYY", style = TextStyle(fontSize = 10.sp)) },
                    onValueChange = { year = it.filter { char -> char.isDigit() }.take(4) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(2f)
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                OutlinedTextField(
                    value = month,
                    label = { Text(text = "MM", style = TextStyle(fontSize = 10.sp)) },
                    onValueChange = { month = it.filter { char -> char.isDigit() }.take(2) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                OutlinedTextField(
                    value = day,
                    label = { Text("DD", style = TextStyle(fontSize = 10.sp)) },
                    onValueChange = { day = it.filter { char -> char.isDigit() }.take(2) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                OutlinedTextField(
                    value = hour,
                    label = { Text("HH", style = TextStyle(fontSize = 10.sp)) },
                    onValueChange = { hour = it.filter { char -> char.isDigit() }.take(2) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                OutlinedTextField(
                    value = minute,
                    label = { Text("mm", style = TextStyle(fontSize = 10.sp)) },
                    onValueChange = { minute = it.filter { char -> char.isDigit() }.take(2) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Take a Picture", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Select Plant Type", fontSize = 16.sp)
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
                        Toast.makeText(context, "Saving in progress, please wait", Toast.LENGTH_SHORT).show()
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.set(
                            year.toInt(),
                            month.toInt() - 1,
                            day.toInt(),
                            hour.toInt(),
                            minute.toInt(),
                            0
                        )
                        val floweringTime = Timestamp(calendar.time)
                        editViewModel.uploadImageToFirebase(capturedImageUri, { downloadUrl ->
                            val newFlower = Flower(
                                name = name.value,
                                image = downloadUrl,
                                floweringTime = floweringTime,
                                type = type
                            )
                            editViewModel.addFlower(context, newFlower)
                        }, { filler ->
                            val newFlower = Flower(
                                name = name.value,
                                image = filler,
                                floweringTime = floweringTime,
                                type = type
                            )
                            editViewModel.addFlower(context, newFlower)
                        })
                    } else {
                        Toast.makeText(context, "Please input valid date", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Select Plant Type") },
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
                                        color = buttonGreen,
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
                        colors = ButtonDefaults.buttonColors(containerColor = buttonGreen)
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}