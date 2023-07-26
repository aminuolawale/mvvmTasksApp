package com.codinginflow.mvvmtodo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Update
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table")
    suspend fun getAll():Flow<List<Task>>
}