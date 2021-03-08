package com.derrick.dstvtodolist.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.derrick.dstvtodolist.dao.TaskDao;
import com.derrick.dstvtodolist.database.TaskRoomDatabase;
import com.derrick.dstvtodolist.models.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> allTasksCompleted;
    private TaskRoomDatabase databaseInstance;

    public TaskRepository(Application application) {
        databaseInstance = TaskRoomDatabase.getDatabaseInstance(application);
        taskDao = databaseInstance.taskDao();
        allTasks = taskDao.getAllTasks();
        allTasksCompleted = taskDao.getAllTasksCompleted();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getAllTasksCompleted() {
        return allTasksCompleted;
    }

    public void addTask(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.addTask(task);
        });
    }

    public void updateTask(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.updateTask(task);
        });
    }

    public void deleteTask(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.deleteTask(task);
        });
    }

    public void deleteTaskById(int id) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.deleteTaskById(id);
        });
    }

    public void deleteAllTasks() {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.deleteAllTasks();
        });
    }


}
