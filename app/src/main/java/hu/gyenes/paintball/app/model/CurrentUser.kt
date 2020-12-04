package hu.gyenes.paintball.app.model

import android.content.Context
import hu.gyenes.paintball.app.model.DTO.UserLoginResponse
import hu.gyenes.paintball.app.utils.Constants.Companion.CURRENT_USER_PREF
import hu.gyenes.paintball.app.utils.Constants.Companion.PREF_EMAIL
import hu.gyenes.paintball.app.utils.Constants.Companion.PREF_EXPIRES_AT
import hu.gyenes.paintball.app.utils.Constants.Companion.PREF_TOKEN
import hu.gyenes.paintball.app.utils.Constants.Companion.PREF_USER_ID
import hu.gyenes.paintball.app.utils.PaintballApplication
import java.util.*

object CurrentUser {
    var userId: String = ""
    var userEmail: String = ""
    var token: String = ""
    var expireAt: Date = Date()
//    val newReservations: MutableList<Reservation> = mutableListOf()

    fun login(loginResponse: UserLoginResponse) {
        userId = loginResponse.localId
        userEmail = loginResponse.email
        token = loginResponse.token
        val cal = Calendar.getInstance()
        cal.add(Calendar.SECOND, loginResponse.expiresIn)
        expireAt = cal.time

        val sharedPref =
            PaintballApplication.context.getSharedPreferences(CURRENT_USER_PREF, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(PREF_USER_ID, userId)
            putString(PREF_EMAIL, userEmail)
            putString(PREF_TOKEN, token)
            putLong(PREF_EXPIRES_AT, expireAt.time)
            apply()
        }
    }
    fun initialize() {
        val sharedPref =
            PaintballApplication.context.getSharedPreferences(CURRENT_USER_PREF, Context.MODE_PRIVATE)
        userId = sharedPref.getString(PREF_USER_ID, "") ?: ""
        userEmail = sharedPref.getString(PREF_EMAIL, "") ?: ""
        token = sharedPref.getString(PREF_TOKEN, "") ?: ""
        expireAt.time = sharedPref.getLong(PREF_EXPIRES_AT, 0L)
    }

    fun isExpired(): Boolean {
        if (token == "" || expireAt <= Date()) return true
        return false
    }
}