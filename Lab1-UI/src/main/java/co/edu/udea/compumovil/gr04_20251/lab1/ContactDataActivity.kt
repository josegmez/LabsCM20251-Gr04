package co.edu.udea.compumovil.gr04_20251.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr04_20251.lab1.ui.theme.Labs20251Gr04Theme
import co.edu.udea.compumovil.gr04_20251.lab1.utils.ValidationUtils.isValidEmail
import co.edu.udea.compumovil.gr04_20251.lab1.utils.ValidationUtils.isValidPhone

data class ContactData(
    val phone: String = "",
    val address: String? = null,
    val email: String = "",
    val country: String = "",
    val city: String? = null
)

class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20251Gr04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val contactData = remember { mutableStateOf(ContactData()) }

                    ContactDataForm(
                        contactData = contactData.value,
                        onContactDataChange = { contactData.value = it },
                        onSubmitClick = { updatedData ->
                            // TODO: Handle submit action to finish the form process
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ContactDataForm(
    contactData: ContactData,
    onContactDataChange: (ContactData) -> Unit,
    onSubmitClick: (ContactData) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // States for input fields
    var phone by remember { mutableStateOf(contactData.phone) }
    var address by remember { mutableStateOf(contactData.address ?: "") }
    var email by remember { mutableStateOf(contactData.email) }
    var country by remember { mutableStateOf(contactData.country) }
    var city by remember { mutableStateOf(contactData.city ?: "") }

    // States for error handling
    var phoneError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var countryError by remember { mutableStateOf(false) }

    val latinAmericanCountries = listOf(
        "Argentina", "Bolivia", "Brasil", "Chile", "Colombia",
        "Costa Rica", "Cuba", "Ecuador", "El Salvador", "Guatemala",
        "Haití", "Honduras", "México", "Nicaragua", "Panamá",
        "Paraguay", "Perú", "República Dominicana", "Uruguay", "Venezuela"
    )

    val colombianCities = listOf(
        "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena",
        "Cúcuta", "Bucaramanga", "Pereira", "Santa Marta", "Ibagué",
        "Pasto", "Manizales", "Neiva", "Villavicencio", "Armenia"
    )

    var countryOptions by remember { mutableStateOf(emptyList<String>()) }
    var cityOptions by remember { mutableStateOf(emptyList<String>()) }

    var isCountryMenuExpanded by remember { mutableStateOf(false) }
    var isCityMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.contact_data),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PhoneInput(
            value = phone,
            onValueChange = {
                phone = it
                phoneError = false
                onContactDataChange(contactData.copy(phone = it))
            },
            isError = phoneError,
            modifier = Modifier.fillMaxWidth()
        )

        AddressInput(
            value = address,
            onValueChange = {
                address = it
                onContactDataChange(contactData.copy(address = if (it.isBlank()) null else it))
            },
            modifier = Modifier.fillMaxWidth()
        )

        EmailInput(
            value = email,
            onValueChange = {
                email = it
                emailError = false
                onContactDataChange(contactData.copy(email = it))
            },
            isError = emailError,
            modifier = Modifier.fillMaxWidth()
        )

        CountryDropdown(
            selectedValue = country,
            onValueChange = {
                country = it
                countryError = false
                countryOptions = latinAmericanCountries.filter { option ->
                    option.contains(it, ignoreCase = true)
                }
                onContactDataChange(contactData.copy(country = it))
            },
            isError = countryError,
            isExpanded = isCountryMenuExpanded,
            onExpandedChange = { isCountryMenuExpanded = it },
            options = countryOptions,
            onOptionSelected = { option ->
                country = option
                countryError = false
                isCountryMenuExpanded = false
                onContactDataChange(contactData.copy(country = option))
            },
            modifier = Modifier.fillMaxWidth()
        )

        CityDropdown(
            selectedValue = city,
            onValueChange = {
                city = it
                cityOptions = colombianCities.filter { option ->
                    option.contains(it, ignoreCase = true)
                }
                onContactDataChange(contactData.copy(city = if (it.isBlank()) null else it))
            },
            isExpanded = isCityMenuExpanded,
            onExpandedChange = { isCityMenuExpanded = it },
            options = cityOptions,
            onOptionSelected = { option ->
                city = option
                isCityMenuExpanded = false
                onContactDataChange(contactData.copy(city = option))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                var hasError = false

                if (!isValidPhone(phone)) {
                    phoneError = true
                    hasError = true
                }

                if (!isValidEmail(email)) {
                    emailError = true
                    hasError = true
                }

                if (country.isBlank()) {
                    countryError = true
                    hasError = true
                }

                if (!hasError) {
                    onSubmitClick(
                        ContactData(
                            phone = phone,
                            address = if (address.isBlank()) null else address,
                            email = email,
                            country = country,
                            city = if (city.isBlank()) null else city
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.submit))
        }
    }
}

@Composable
fun PhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.phone)) },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.required_field))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        singleLine = true,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun AddressInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.address)) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        singleLine = true,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun EmailInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.email)) },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.required_field))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        singleLine = true,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdown(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.country)) },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(stringResource(R.string.required_field))
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            singleLine = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { onOptionSelected(option) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CityDropdown(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.city)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            singleLine = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { onOptionSelected(option) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactDataFormPreview() {
    Labs20251Gr04Theme {
        ContactDataForm(
            contactData = ContactData(),
            onContactDataChange = {},
            onSubmitClick = {}
        )
    }
}