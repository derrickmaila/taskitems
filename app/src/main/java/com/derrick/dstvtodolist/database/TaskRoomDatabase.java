package com.derrick.dstvtodolist.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.derrick.dstvtodolist.dao.TaskDao;
import com.derrick.dstvtodolist.models.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1,exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {
    public  abstract TaskDao taskDao();
    private static volatile TaskRoomDatabase taskRoomDatabaseInstance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static  TaskRoomDatabase getDatabaseInstance(final Context context){
        if(taskRoomDatabaseInstance == null){
            taskRoomDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskRoomDatabase.class,"task_database")
                    .build();
        }
        return taskRoomDatabaseInstance;
    }

}
