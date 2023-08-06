package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) // Module should be available at the application level
object AppModule {
    @Provides
    @Singleton // Tells daggerHilt to create only one instance of this object
    fun provideDatabase(app: Application, callback: TaskDatabase.Callback) =
        Room.databaseBuilder(app, TaskDatabase::class.java, "task_table")
            .fallbackToDestructiveMigration().addCallback(callback).build()

    // Doesn't need a singleton annotation because this returns a reference to a member object of the singleton db
    @Provides
    fun providesTaskDao(db: TaskDatabase) = db.taskDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope