import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundGreen
import com.example.plantparenthood.ui.theme.buttonGreen
import com.example.plantparenthood.utils.AuthHelper

@Composable
fun LoginScreen(context: Context, navController: NavController, loginViewModel: LoginViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val stayLoggedIn = remember { mutableStateOf(AuthHelper.isRememberMeChecked(context)) }

    LaunchedEffect(Unit) {
        if (stayLoggedIn.value) {
            val credentials = AuthHelper.loadCredentials(context)
            email.value = credentials.first
            password.value = credentials.second
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
                onClick = { navController.navigate("welcome_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .wrapContentWidth()
            ) {
                Text("Return", fontSize = 16.sp)
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 360.dp, height = 340.dp)
                    .clip(shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Login:",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
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
                    onCheckedChange = { stayLoggedIn.value = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Remember Me",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable {
                        stayLoggedIn.value = !stayLoggedIn.value
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { loginViewModel.signIn(context, email.value, password.value, stayLoggedIn.value, navController) },
                colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text("Login", fontSize = 16.sp)
            }
        }
    }
}
