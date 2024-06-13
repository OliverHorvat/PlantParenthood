import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.plantparenthood.Flower
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class EditViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val collectionRef = db.collection(currentUser)
    val storage = FirebaseStorage.getInstance()
    val storageRef: StorageReference = storage.reference
    val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
    fun addFlower(context: Context, flower: Flower) {
        collectionRef.add(flower)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, flower.name+" has been added to your garden", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to add flower to your garden", Toast.LENGTH_SHORT).show()
            }
    }
    fun uploadImageToFirebase(uri: Uri, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        imageRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { filler ->
                onFailure("")
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
