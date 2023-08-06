package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    // the onCreate is called after the database object is already constructed so we can have a reference to it
    // within this callback even as the database object depends on this callback to be constructed.
    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // db operations
            val dao = database.get().taskDao()
            applicationScope.launch {
                listOf("Task One", "Task Two", "Task three", "Task four", "Task five").forEach {
                    dao.insert(
                        Task(
                            name = it,
                            isImportant = Random.nextBoolean(),
                            isCompleted = Random.nextBoolean()
                        )
                    )
                }

            }
        }
    }
}