package com.example.plantparenthood

import com.google.firebase.Timestamp

data class Flower(
    val image: String = "",
    val name: String = "",
    val type: String = "",
    val floweringTime: Timestamp = Timestamp(0,0)
)
