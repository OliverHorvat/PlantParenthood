import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.plantparenthood.Flower
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class EditViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val collectionRef = db.collection(currentUser)

    fun addFlower(context: Context, flower: Flower) {
        collectionRef.add(flower)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, flower.name+" has been added to your garden", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to add flower to your garden", Toast.LENGTH_SHORT).show()
            }
    }
}
