package co.edu.udea.compumovil.gr04_20251.lab1.utils

import android.util.Log
import co.edu.udea.compumovil.gr04_20251.lab1.ContactData
import co.edu.udea.compumovil.gr04_20251.lab1.PersonalData

object PrintUtils {
  fun printPersonalData(data: PersonalData) {
    Log.d("Lab1", "Información personal:")
    Log.d("Lab1", "${data.firstName} ${data.lastName}")
    Log.d("Lab1", if (data.gender.length > 0) data.gender else "No especificado")
    Log.d("Lab1", "Nació el ${data.dateOfBirth}")
    Log.d("Lab1", if (data.educationLevel.length > 0) data.educationLevel else "No especificado")
  }

  fun printContactData(data: ContactData) {
    Log.d("Lab1", "Información de contacto:")
    Log.d("Lab1", "Teléfono: ${data.phone}")
    Log.d("Lab1", "Dirección: ${data.address ?: "No especificada"}")
    Log.d("Lab1", "Email: ${data.email}")
    Log.d("Lab1", "País: ${data.country}")
    Log.d("Lab1", "Ciudad: ${data.city ?: "No especificada"}")
  }
}
