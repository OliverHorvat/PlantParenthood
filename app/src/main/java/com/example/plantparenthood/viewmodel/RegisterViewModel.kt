package com.example.plantparenthood

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(context: Context, email: String, password: String, navController: NavController) {
        viewModelScope.launch {
            if (email != "" && password != "") {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { registerTask ->
                        if (registerTask.isSuccessful) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { signInTask ->
                                    if (signInTask.isSuccessful) {
                                        Toast.makeText(context, "Registration and login successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("main_screen")
                                    } else {
                                        Toast.makeText(context, "Login after registration failed", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login_screen")
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
