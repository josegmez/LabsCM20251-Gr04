package co.edu.udea.compumovil.gr04_20251.lab1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr04_20251.lab1.ui.theme.Labs20251Gr04Theme
import java.text.SimpleDateFormat
import java.util.Calendar

data class PersonalData(
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val dateOfBirth: String = "",
    val educationLevel: String = ""
)

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20251Gr04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val personalData = remember { mutableStateOf(PersonalData()) }

                    PersonalDataForm(
                        personalData = personalData.value,
                        onPersonalDataChange = { personalData.value = it },
                        onNextClick = { updatedData ->
                            // TODO: Handle the next action to navigate to the contact data screen
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
fun PersonalDataForm(
    personalData: PersonalData,
    onPersonalDataChange: (PersonalData) -> Unit,
    onNextClick: (PersonalData) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val scrollState = rememberScrollState()

    // States for input fields
    var firstName by remember { mutableStateOf(personalData.firstName) }
    var lastName by remember { mutableStateOf(personalData.lastName) }
    var gender by remember { mutableStateOf(personalData.gender) }
    var dateOfBirth by remember { mutableStateOf(personalData.dateOfBirth) }
    var educationLevel by remember { mutableStateOf(personalData.educationLevel) }

    // States for error handling
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var dateOfBirthError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.personal_data),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            NameInput(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = false
                    onPersonalDataChange(personalData.copy(firstName = it))
                },
                isError = firstNameError,
                modifier = Modifier.fillMaxWidth()
            )

            LastNameInput(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = false
                    onPersonalDataChange(personalData.copy(lastName = it))
                },
                isError = lastNameError,
                modifier = Modifier.fillMaxWidth()
            )

            SexSelector(
                selectedGender = gender,
                onGenderSelected = {
                    gender = it
                    onPersonalDataChange(personalData.copy(gender = it))
                },
                modifier = Modifier.fillMaxWidth()
            )

            BirthDateInput(
                value = dateOfBirth,
                isError = dateOfBirthError,
                onDateSelected = { date ->
                    dateOfBirth = date
                    dateOfBirthError = false
                    onPersonalDataChange(personalData.copy(dateOfBirth = date))
                },
                modifier = Modifier.fillMaxWidth()
            )

            SchoolingDropdownMenu(
                selectedValue = educationLevel,
                onValueSelected = {
                    educationLevel = it
                    onPersonalDataChange(personalData.copy(educationLevel = it))
                },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NameInput(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        firstNameError = false
                        onPersonalDataChange(personalData.copy(firstName = it))
                    },
                    isError = firstNameError,
                    modifier = Modifier.weight(1f)
                )

                LastNameInput(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        lastNameError = false
                        onPersonalDataChange(personalData.copy(lastName = it))
                    },
                    isError = lastNameError,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BirthDateInput(
                    value = dateOfBirth,
                    isError = dateOfBirthError,
                    onDateSelected = { date ->
                        dateOfBirth = date
                        dateOfBirthError = false
                        onPersonalDataChange(personalData.copy(dateOfBirth = date))
                    },
                    modifier = Modifier.weight(1f)
                )

                SchoolingDropdownMenu(
                    selectedValue = educationLevel,
                    onValueSelected = {
                        educationLevel = it
                        onPersonalDataChange(personalData.copy(educationLevel = it))
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            SexSelector(
                selectedGender = gender,
                onGenderSelected = {
                    gender = it
                    onPersonalDataChange(personalData.copy(gender = it))
                }
            )
        }

        Button(
            onClick = {
                var hasError = false

                if (firstName.isBlank()) {
                    firstNameError = true
                    hasError = true
                }

                if (lastName.isBlank()) {
                    lastNameError = true
                    hasError = true
                }

                if (dateOfBirth.isBlank()) {
                    dateOfBirthError = true
                    hasError = true
                }

                if (!hasError) {
                    onNextClick(
                        PersonalData(
                            firstName = firstName,
                            lastName = lastName,
                            gender = gender,
                            dateOfBirth = dateOfBirth,
                            educationLevel = educationLevel
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}

@Composable
fun NameInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.first_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                modifier = Modifier.padding(4.dp)
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.required_field))
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Next
                )
            }),
        modifier = modifier
    )
}

@Composable
fun LastNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.last_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "Person Icon",
                modifier = Modifier.padding(4.dp)
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.required_field))
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Next
                )
            }),
        modifier = modifier
    )
}

@Composable
fun SexSelector(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val maleRes = stringResource(R.string.male)
    val femaleRes = stringResource(R.string.female)

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.gender),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (selectedGender == maleRes),
                onClick = { onGenderSelected(maleRes) }
            )
            Text(
                text = maleRes,
                modifier = Modifier.padding(end = 16.dp)
            )

            RadioButton(
                selected = (selectedGender == femaleRes),
                onClick = { onGenderSelected(femaleRes) }
            )
            Text(text = femaleRes)
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun BirthDateInput(
    value: String,
    isError: Boolean,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
    )
    var isOpen by remember { mutableStateOf(false) }
    val source = remember {
        MutableInteractionSource()
    }

    OutlinedTextField(
        value = value,
        onValueChange = { /* Read-only */ },
        label = { Text(stringResource(R.string.date_of_birth)) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = stringResource(R.string.select_date),
                modifier = Modifier.padding(4.dp)
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.required_field))
            }
        },
        readOnly = true,
        interactionSource = source,
        modifier = modifier
    )

    if (source.collectIsPressedAsState().value) {
        isOpen = true
    }

    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = { isOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = SimpleDateFormat("dd/MM/yyyy").format(it)
                        onDateSelected(date)
                    }
                    isOpen = false
                }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isOpen = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        stringResource(R.string.select_date),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolingDropdownMenu(
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        stringResource(R.string.primary),
        stringResource(R.string.secondary),
        stringResource(R.string.technical),
        stringResource(R.string.university),
        stringResource(R.string.postgraduate)
    )
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { /* Read-only */ },
            readOnly = true,
            label = {
                Text(stringResource(R.string.education_level))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level) },
                    onClick = {
                        onValueSelected(level)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDataFormPreview() {
    Labs20251Gr04Theme {
        PersonalDataForm(
            personalData = PersonalData(),
            onPersonalDataChange = {},
            onNextClick = {}
        )
    }
}
