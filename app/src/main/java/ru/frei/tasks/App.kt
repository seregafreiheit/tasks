package ru.frei.tasks

import android.app.Application
import ru.frei.tasks.data.AppDatabase

class App : Application() {
    val database: AppDatabase by lazy { AppDatabase.buildDatabase(this) }
}