import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guardiancare.BackEnd.AuthService
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("elderly") }
    var message by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Row {
            RadioButton(selected = role == "elderly", onClick = { role = "elderly" })
            Text("Elderly")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = role == "member", onClick = { role = "member" })
            Text("Member")
        }

        Button(onClick = {
            coroutineScope.launch {
                try {
                    val response = AuthService.register(name, email, password, role)
                    message = response ?: "Registration Failed"
                } catch (e: Exception) {
                    message = "Error: ${e.localizedMessage}"  // Properly display error messages
                }
            }
        }) {
            Text("Register")
        }

        if (message.isNotEmpty()) {
            Text(text = message, color = androidx.compose.ui.graphics.Color.Red)
        }
    }
}
