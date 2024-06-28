package com.example.plantparenthood

import com.google.firebase.Timestamp

data class Plant(
    var image: String = "",
    var name: String = "",
    var type: String = "",
    var wateringTime: Timestamp = Timestamp(0,0),
    var documentId: String = ""
    )
