import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.plantparenthood.Flower
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

class EditViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val collectionRef = db.collection("users").document(currentUser).collection("flowers")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference
    private val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
    fun addFlower(context: Context, flower: Flower) {
        collectionRef.add(flower)
            .addOnSuccessListener { documentReference ->
                val updatedFlower = flower.copy(documentId = documentReference.id)
                collectionRef.document(documentReference.id).set(updatedFlower)
                    .addOnSuccessListener {
                        Toast.makeText(context, "${flower.name} has been added to your garden", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "${flower.name} is not properly added to garden", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add flower to your garden", Toast.LENGTH_SHORT).show()
            }
    }

    fun editFlower(context: Context, documentId: String, flower: Flower) {
        val updatedFlower = flower.copy(documentId = documentId)
        collectionRef.document(documentId).set(updatedFlower)
            .addOnSuccessListener {
                Toast.makeText(context, "${flower.name} has been updated in your garden", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update flower in your garden", Toast.LENGTH_SHORT).show()
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
}
