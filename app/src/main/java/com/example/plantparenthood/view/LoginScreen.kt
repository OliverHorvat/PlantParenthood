import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
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

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Login",
                color = Color.Black,
                fontSize = 44.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(84.dp))

            Text(
                text = "Email",
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Password",
                color = Color.Black,
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Checkbox(
                    checked = stayLoggedIn.value,
                    onCheckedChange = { stayLoggedIn.value = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Remember Me", fontSize = 16.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(52.dp))

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
