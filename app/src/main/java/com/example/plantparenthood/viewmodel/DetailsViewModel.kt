import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.plantparenthood.PlantType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DetailsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getTypeDetails(context: Context, type: String): PlantType? {
        try {
            val documentSnapshot = db.collection("plantType").document(type).get().await()
            if (documentSnapshot.exists()) {
                val image = documentSnapshot.getString("image") ?: ""
                val description = documentSnapshot.getString("description") ?: ""

                return PlantType(image, description)
            } else {
                Toast.makeText(context, "Something went wrong, please check your internet connection", Toast.LENGTH_SHORT).show()
                return null
            }
        }catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Something went wrong, please check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }
        return null;
    }
}
