package co.edu.udea.compumovil.gr04_20251.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.udea.compumovil.gr04_20251.lab1.ui.theme.Labs20251Gr04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20251Gr04Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var personalData by remember { mutableStateOf(PersonalData()) }
    var contactData by remember { mutableStateOf(ContactData()) }

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "personal_data",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("personal_data") {
                PersonalDataForm(
                    personalData = personalData,
                    onPersonalDataChange = { personalData = it },
                    onNextClick = { validData ->
                        // TODO: Print personal data
                        personalData = validData
                        navController.navigate("contact_data")
                    }
                )
            }
            composable("contact_data") {
                ContactDataForm(
                    contactData = contactData,
                    onContactDataChange = { contactData = it },
                    onSubmitClick = { validData ->
                        // TODO: Print contact data
                        contactData = validData
                    }
                )
            }
        }
    }
}