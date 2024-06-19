import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.plantparenthood.Flower
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GardenViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private var flowers = mutableStateOf<List<Flower>>(emptyList())
    var screenFlower = Flower()
    suspend fun getFlowers(context: Context): List<Flower> = withContext(Dispatchers.IO) {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
            val collectionRef = FirebaseFirestore.getInstance().collection("users").document(currentUser).collection("flowers")
            val querySnapshot = collectionRef.get().await()
            val newFlowers = mutableListOf<Flower>()
            for (document in querySnapshot.documents) {
                val image = document.getString("image") ?: ""
                val name = document.getString("name") ?: ""
                val type = document.getString("type") ?: ""
                val floweringTime = document.getTimestamp("floweringTime") ?: Timestamp(0, 0)
                newFlowers.add(
                    Flower(
                        image = image,
                        name = name,
                        type = type,
                        floweringTime = floweringTime
                    )
                )
            }
            val sortedFlowers = newFlowers.sortedBy { it.name }
            flowers.value = sortedFlowers
            sortedFlowers
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to add flower to your garden", Toast.LENGTH_SHORT).show()
            }
            emptyList()
        }
    }

    fun goToFlower(flower: Flower, navController: NavController){
        screenFlower = flower
        navController.navigate("plant_screen")
    }
}
