import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.plantparenthood.Plant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GardenViewModel : ViewModel() {
    private var plants = mutableStateOf<List<Plant>>(emptyList())
    var screenPlantId = ""
    var screenPlantType = ""
    suspend fun getPlants(context: Context): List<Plant> = withContext(Dispatchers.IO) {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
            val collectionRef = FirebaseFirestore.getInstance().collection("users").document(currentUser).collection("plants")
            val querySnapshot = collectionRef.get().await()
            val newPlants = mutableListOf<Plant>()
            for (document in querySnapshot.documents) {
                val image = document.getString("image") ?: ""
                val name = document.getString("name") ?: ""
                val type = document.getString("type") ?: ""
                newPlants.add(
                    Plant(
                        image = image,
                        name = name,
                        type = type,
                        documentId = document.id
                    )
                )
            }
            val sortedPlants = newPlants.sortedBy { it.name }
            plants.value = sortedPlants
            sortedPlants
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to add plant to your garden", Toast.LENGTH_SHORT).show()
            }
            emptyList()
        }
    }

    fun goToPlant(plant: Plant, navController: NavController){
        screenPlantId = plant.documentId
        screenPlantType = plant.type
        navController.navigate("plant_screen")
    }
}
