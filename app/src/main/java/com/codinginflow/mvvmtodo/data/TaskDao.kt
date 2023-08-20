package com.codinginflow.mvvmtodo.data

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' AND (isCompleted != :hideCompletedTasks  or isCompleted = 0) ORDER BY isImportant DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompletedTasks: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' AND (isCompleted != :hideCompletedTasks  or isCompleted = 0) ORDER BY isImportant DESC, created")
    fun getTasksSortedByDate(searchQuery: String, hideCompletedTasks: Boolean): Flow<List<Task>>

    @Query("DELETE FROM task_table WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()

    fun getTasks(searchQuery: String, filterPreferences: FilterPreferences): Flow<List<Task>>{
        return when (filterPreferences.sortOrder) {
            SortOrder.BY_NAME -> getTasksSortedByName(searchQuery, filterPreferences.hideCompleted)
            SortOrder.BY_DATE -> getTasksSortedByDate(searchQuery, filterPreferences.hideCompleted)
        }
    }
}