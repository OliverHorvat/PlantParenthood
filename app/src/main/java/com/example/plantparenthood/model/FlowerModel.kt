package com.example.plantparenthood

import com.google.firebase.Timestamp

data class Flower(
    var image: String = "",
    var name: String = "",
    var type: String = "",
    var floweringTime: Timestamp = Timestamp(0,0),
    var documentId: String = ""
    )
