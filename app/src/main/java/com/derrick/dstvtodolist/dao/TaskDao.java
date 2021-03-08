package com.derrick.dstvtodolist.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.derrick.dstvtodolist.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTask(Task task);
    @Update
    void updateTask(Task task);
    @Delete
    void deleteTask(Task task);
    @Query("Delete from task_table where id=:id")
    void deleteTaskById(int id);
    @Query("Delete from task_table")
    void deleteAllTasks();
    @Query("Select * from task_table ORDER BY id DESC")
    LiveData<List<Task>> getAllTasks();
    @Query("Select * from task_table where task_status ='Yes'")
    LiveData<List<Task>> getAllTasksCompleted();

}
