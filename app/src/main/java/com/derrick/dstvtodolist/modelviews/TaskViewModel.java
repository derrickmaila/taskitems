package com.derrick.dstvtodolist.modelviews;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.derrick.dstvtodolist.models.Task;
import com.derrick.dstvtodolist.repositories.TaskRepository;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    public final LiveData<List<Task>> allTasks;
    public final  LiveData<List<Task>> allTasksCompleted;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
        allTasksCompleted = taskRepository.getAllTasksCompleted();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }
    public LiveData<List<Task>> getAllTasksCompleted(){
        return allTasksCompleted;
    }
    public void addTask(Task task){
        taskRepository.addTask(task);
    }
    public void updateTask(Task task){
        taskRepository.updateTask(task);
    }
    public void deleteTask(Task task){
        taskRepository.deleteTask(task);
    }
    public void deleteTaskById(int id){
        taskRepository.deleteTaskById(id);
    }
    public void deleteAllTasks(){
        taskRepository.deleteAllTasks();
    }

}
