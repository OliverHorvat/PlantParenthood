import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.plantparenthood.Plant
import com.example.plantparenthood.utils.NotificationUtils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import kotlin.math.abs

class PlantViewModel : ViewModel() {
    var type = ""
    private val db = FirebaseFirestore.getInstance()
    suspend fun getPlantById(context:Context, documentId: String): Plant? {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
            val documentSnapshot = db.collection("users").document(currentUser).collection("plants").document(documentId).get().await()

            if (documentSnapshot.exists()) {
                val image = documentSnapshot.getString("image") ?: ""
                val name = documentSnapshot.getString("name") ?: ""
                val type = documentSnapshot.getString("type") ?: ""
                val wateringTime = documentSnapshot.getTimestamp("wateringTime") ?: Timestamp(0, 0)

                return Plant(
                    image = image,
                    name = name,
                    type = type,
                    wateringTime = wateringTime,
                    documentId = documentSnapshot.id,
                    ownerId = currentUser
                )
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return null
    }

    suspend fun updateWateringTime(context: Context, plant: Plant, daysBetweenWatering: Int) {

        val newTimestamp = Timestamp.now()
        val wateringTimeUpdate = mapOf(
            "wateringTime" to newTimestamp
        )
        plant.wateringTime = newTimestamp
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
            val collectionRef = db.collection("users").document(currentUser).collection("plants")
            collectionRef.document(plant.documentId).update(wateringTimeUpdate).await()
            NotificationUtils.cancelNotification(context, plant.documentId)
            NotificationUtils.scheduleNotification(context, plant, daysBetweenWatering)
            Toast.makeText(context, "Watering time has been updated", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to update watering time", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun deletePlantById(context: Context, documentId: String, imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val documentRef = db.collection("users").document(currentUser).collection("plants").document(documentId)
        val documentSnapshot = documentRef.get().await()

        if (documentSnapshot.exists()) {
            documentRef.delete().await()
            NotificationUtils.cancelNotification(context, documentId)
            if (imageUrl != "") {
                val startIndex = imageUrl.indexOf("images%2F") + "images%2F".length
                val endIndex = imageUrl.indexOf(".jpg", startIndex) + ".jpg".length
                val imageId = imageUrl.substring(startIndex, endIndex)
                val storage = Firebase.storage
                val imageRef = storage.reference.child("images/$imageId")
                imageRef.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("DeleteImage", "Image is deleted successfully")
                        } else {
                            Log.d("DeleteImage", "Image is not deleted successfully")
                        }
                    }
            }
            Toast.makeText(context, "Plant deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Something went wrong, please check your internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun fetchDaysBetweenWatering(context: Context, plantType: String): Int {
        var daysBetweenWatering = 0
        val daysBetweenWateringDocRef = db.collection("plantType").document(plantType)

        try {
            val document = daysBetweenWateringDocRef.get().await()
            if (document.exists()) {
                daysBetweenWatering = document.getLong("daysBetweenWatering")?.toInt() ?: 0
            }
        } catch (e: Exception) {
            daysBetweenWatering = 0
            Toast.makeText(context, "Something went wrong, please check your internet connection", Toast.LENGTH_SHORT).show()
        }
        return daysBetweenWatering
    }

    fun calculateWateringTime(wateringTime: Timestamp, daysBetweenWatering: Int): Pair<Boolean, Int> {
        val currentTimeMillis = System.currentTimeMillis()
        val lastWateringTimeMillis = wateringTime.seconds * 1000 + wateringTime.nanoseconds / 1000000
        val daysInMillis = 1000 * 60 * 60 * 24

        val nextWateringTimeMillis = lastWateringTimeMillis + daysBetweenWatering * daysInMillis
        val overdue = currentTimeMillis > nextWateringTimeMillis
        val minutesDifference = abs(((currentTimeMillis - nextWateringTimeMillis) / (1000 * 60))).toInt()

        return Pair(overdue, minutesDifference)
    }
}

