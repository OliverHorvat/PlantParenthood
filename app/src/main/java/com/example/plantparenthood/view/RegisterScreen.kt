import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundDark
import com.example.plantparenthood.ui.theme.backgroundLight
import com.example.plantparenthood.ui.theme.buttonDark
import com.example.plantparenthood.ui.theme.buttonLight
import com.example.plantparenthood.utils.AuthHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(context: Context, navController: NavController, registerViewModel: RegisterViewModel) {
    var backgroundColor = backgroundLight
    var buttonColor = buttonLight
    var textColor = Color.Black

    if(isSystemInDarkTheme()){
        backgroundColor = backgroundDark
        buttonColor = buttonDark
        textColor = Color.White
    }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val stayLoggedIn = remember { mutableStateOf(AuthHelper.isRememberMeChecked(context)) }

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
            Button(
                onClick = { navController.navigate("welcome_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .wrapContentWidth()
            ) {
                Text("Return", fontSize = 16.sp, color = Color.White)
            }

            Image(
                painter = painterResource(id = R.drawable.logo_transparent),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 360.dp, height = 340.dp)
                    .clip(shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Register:",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email", color = textColor) },
                textStyle = TextStyle(color = textColor),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor,
                    cursorColor = textColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password", color = textColor) },
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = textColor),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor,
                    cursorColor = textColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Checkbox(
                    checked = stayLoggedIn.value,
                    onCheckedChange = { stayLoggedIn.value = it },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = textColor,
                        checkedColor = buttonColor,
                        checkmarkColor = textColor
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Remember Me",
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.clickable {
                        stayLoggedIn.value = !stayLoggedIn.value
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { registerViewModel.register(context, email.value, password.value, stayLoggedIn.value, navController) },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Register", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}