package hu.gyenes.paintball.app.utils

import android.app.Application
import android.content.Context
import hu.gyenes.paintball.app.model.CurrentUser

class PaintballApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        CurrentUser.initialize()
    }
}