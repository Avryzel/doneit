package id.jel.doneit

import android.app.Application
import id.jel.doneit.data.local.AppDatabase

class DoneItApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}