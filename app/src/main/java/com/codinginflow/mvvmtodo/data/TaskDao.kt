package com.codinginflow.mvvmtodo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codinginflow.mvvmtodo.ui.tasks.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Update
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' AND (isCompleted != :hideCompletedTasks  or isCompleted = 0) ORDER BY isImportant DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompletedTasks: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' AND (isCompleted != :hideCompletedTasks  or isCompleted = 0) ORDER BY isImportant DESC, created")
    fun getTasksSortedByDate(searchQuery: String, hideCompletedTasks: Boolean): Flow<List<Task>>

    fun getTasks(searchQuery: String, sortOrder: SortOrder, hideCompletedTasks: Boolean): Flow<List<Task>>{
        return when (sortOrder) {
            SortOrder.BY_NAME -> getTasksSortedByName(searchQuery, hideCompletedTasks)
            SortOrder.BY_DATE -> getTasksSortedByDate(searchQuery, hideCompletedTasks)
        }
    }
}