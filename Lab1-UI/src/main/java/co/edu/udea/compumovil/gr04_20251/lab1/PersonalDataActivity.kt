package co.edu.udea.compumovil.gr04_20251.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr04_20251.lab1.ui.theme.Labs20251Gr04Theme
import java.text.SimpleDateFormat

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20251Gr04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PersonalDataForm(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataForm(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        NameInput()
        LastNameInput()
        SexSelector()
        BirthDateInput(
            onDateSelected = { date ->
                // Handle the selected date
            },
            onDismiss = {
                // Handle the dialog dismissal
            }
        )
    }
}

@Composable
fun NameInput() {
    val name = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = name.value,
        onValueChange = { name.value = it },
        label = { Text("Name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                modifier = Modifier.padding(4.dp)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Down
                )
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )

}

@Composable
fun LastNameInput() {
    val lastName = remember { mutableStateOf("") }

    OutlinedTextField(
        value = lastName.value,
        onValueChange = { lastName.value = it },
        label = { Text("Last Name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "Person Icon",
                modifier = Modifier.padding(4.dp)
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SexSelector() {
    val sexOptions = listOf("Male", "Female")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(sexOptions[0]) }

    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Sex:")
        sexOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(4.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(option)
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun BirthDateInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
    )
    val isOpen = remember { mutableStateOf(false) }
    val source = remember {
        MutableInteractionSource()
    }

    OutlinedTextField(
        value = datePickerState.selectedDateMillis?.let {
            SimpleDateFormat("dd/MM/yyyy").format(it)
        } ?: "",
        label = { Text("Birth Date") },
        onValueChange = { },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "Person Icon",
                modifier = Modifier.padding(4.dp)
            )
        },
        readOnly = true,
        interactionSource = source,
        modifier = Modifier.fillMaxWidth()
    )

    if (source.collectIsPressedAsState().value) {
        isOpen.value = true
    }

    if (isOpen.value) {

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    isOpen.value = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                    isOpen.value = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Labs20251Gr04Theme {
        PersonalDataForm()
    }
}