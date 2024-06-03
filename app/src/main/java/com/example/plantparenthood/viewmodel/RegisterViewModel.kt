package com.example.plantparenthood

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun register(context: Context, email: String, password: String, navController: NavController) {
        viewModelScope.launch {
            if (email != "" && password != "") {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { registerTask ->
                        if (registerTask.isSuccessful) {
                            val user = auth.currentUser
                            user?.let { currentUser ->
                                val userID = currentUser.uid
                                val userData = hashMapOf(
                                    "email" to email
                                )

                                db.collection(userID)
                                    .document("userData")
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Registration and login successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("main_screen")
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Failed to add user data", Toast.LENGTH_SHORT).show()
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