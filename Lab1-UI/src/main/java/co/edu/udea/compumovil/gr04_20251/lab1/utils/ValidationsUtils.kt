package co.edu.udea.compumovil.gr04_20251.lab1.utils

import android.util.Patterns


/**
 * Utility functions for validations in Contact Data Activity.
 */
object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.isNotBlank() && Patterns.PHONE.matcher(phone).matches()
    }
}