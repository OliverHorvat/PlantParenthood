import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
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


@Composable
fun EditScreen(context: Context, navController: NavController, editViewModel: EditViewModel) {
    val name = remember { mutableStateOf("") }
    val image: String = ""
    val type = "orhideja"
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }

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

            Image(
                painter = painterResource(id = R.drawable.a),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.CenterHorizontally)
            )

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
                onClick = { /* Handle insert image action */ },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Insert Image", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle select plant type action */ },
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
                    val calendar = Calendar.getInstance()
                    calendar.set(year.toInt(), month.toInt() - 1, day.toInt(), hour.toInt(), minute.toInt(), 0)
                    val floweringTime = Timestamp(calendar.time)

                    val newFlower = Flower(
                        name = name.value,
                        image = image,
                        floweringTime = floweringTime,
                        type = type
                    )
                    editViewModel.addFlower(context, newFlower)
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
    }
}
