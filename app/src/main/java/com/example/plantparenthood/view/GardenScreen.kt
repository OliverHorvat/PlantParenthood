import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantparenthood.R
import com.example.plantparenthood.ui.theme.backgroundGreen
import com.example.plantparenthood.ui.theme.buttonGreen

data class ListItem(val name: String, val imageResId: Int)

@Composable
fun GardenScreen() {
    val items = listOf(
        ListItem("Saša", R.drawable.a),
        ListItem("Tin", R.drawable.a),
        ListItem("Kedžo", R.drawable.a)
    )

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGreen)
        ) {
            Column {
                Button(
                    onClick = { /* Handle return action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonGreen),
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, bottom = 16.dp)
                        .wrapContentWidth()
                ) {
                    Text("Return", fontSize = 16.sp)
                }
                RecyclerView(items)
            }
        }
    }
}

@Composable
fun RecyclerView(items: List<ListItem>) {
    LazyColumn {
        items(items) { item ->
            ListItemView(item = item)
        }
    }
}

@Composable
fun ListItemView(item: ListItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click action */ }
            .background(buttonGreen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}
