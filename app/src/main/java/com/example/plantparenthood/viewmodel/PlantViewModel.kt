import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.plantparenthood.Flower
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.math.abs

class PlantViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getFlowerById(context:Context, documentId: String): Flower? {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
            val documentSnapshot = db.collection("users").document(currentUser).collection("flowers").document(documentId).get().await()

            if (documentSnapshot.exists()) {
                val image = documentSnapshot.getString("image") ?: ""
                val name = documentSnapshot.getString("name") ?: ""
                val type = documentSnapshot.getString("type") ?: ""
                val floweringTime = documentSnapshot.getTimestamp("floweringTime") ?: Timestamp(0, 0)

                return Flower(
                    image = image,
                    name = name,
                    type = type,
                    floweringTime = floweringTime,
                    documentId = documentSnapshot.id
                )
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        return null
    }
    suspend fun fetchDaysBetweenWatering(context: Context, flowerType: String): Int {
        var daysBetweenWatering = 0
        val daysBetweenWateringDocRef = db.collection("plantType").document(flowerType)

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

    fun calculateWateringTime(floweringTime: Timestamp, daysBetweenWatering: Int): Pair<Boolean, Int> {
        val currentTimeMillis = System.currentTimeMillis()
        val lastFloweringTimeMillis = floweringTime.seconds * 1000 + floweringTime.nanoseconds / 1000000
        val daysInMillis = 1000 * 60 * 60 * 24

        val nextWateringTimeMillis = lastFloweringTimeMillis + daysBetweenWatering * daysInMillis
        val overdue = currentTimeMillis > nextWateringTimeMillis
        val minutesDifference = abs(((currentTimeMillis - nextWateringTimeMillis) / (1000 * 60))).toInt()

        return Pair(overdue, minutesDifference)
    }
}

