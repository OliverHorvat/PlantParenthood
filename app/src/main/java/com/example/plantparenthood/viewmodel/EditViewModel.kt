import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantparenthood.Plant
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import com.example.plantparenthood.utils.NotificationUtils
import com.example.plantparenthood.utils.NotificationUtils.cancelNotification
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val collectionRef = db.collection("users").document(currentUser).collection("plants")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference
    private val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
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
    fun addPlant(context: Context, plant: Plant) {
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        collectionRef.add(plant)
            .addOnSuccessListener { documentReference ->
                val updatedPlant = plant.copy(documentId = documentReference.id, ownerId = currentUser)
                collectionRef.document(documentReference.id).set(updatedPlant)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            Toast.makeText(context, "${plant.name} has been added to your garden", Toast.LENGTH_SHORT).show()
                            NotificationUtils.scheduleNotification(context, updatedPlant, fetchDaysBetweenWatering(context, plant.type))
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "${plant.name} is not properly added to garden", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add plant to your garden", Toast.LENGTH_SHORT).show()
            }
    }

    fun editPlant(context: Context, documentId: String, plant: Plant) {
        collectionRef.document(documentId).set(plant)
            .addOnSuccessListener {
                viewModelScope.launch {
                    Toast.makeText(context, "${plant.name} has been updated in your garden", Toast.LENGTH_SHORT).show()
                    cancelNotification(context, plant.documentId)
                    NotificationUtils.scheduleNotification(context, plant, fetchDaysBetweenWatering(context, plant.type))
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update plant in your garden", Toast.LENGTH_SHORT).show()
            }
    }

    fun uploadImageToFirebase(uri: Uri, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { filler ->
                onFailure("")
            }
    }
    fun deleteImageFromFirebase(imageUrl: String) {
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

    suspend fun getPlantTypes(): List<String> = withContext(Dispatchers.IO) {
        try {
            val plantTypesCollection = db.collection("plantType")
            val querySnapshot = plantTypesCollection.get().await()
            querySnapshot.documents.map { it.id }
        } catch (e: Exception) {
            emptyList()
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
}
