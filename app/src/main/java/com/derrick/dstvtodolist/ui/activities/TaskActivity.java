package com.derrick.dstvtodolist.ui.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.derrick.dstvtodolist.R;
import com.derrick.dstvtodolist.adapters.TaskAdapter;
import com.derrick.dstvtodolist.databinding.ActivityTaskBinding;
import com.derrick.dstvtodolist.models.Task;
import com.derrick.dstvtodolist.modelviews.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    public ActivityTaskBinding activityTaskBinding;
    // List<Task> tasks;
    public int totalTasks;
    public int tasksCompleted;
    private TaskViewModel taskViewModel;
    public static final int TASK_ACTIVITY_REQUEST_CODE = 1;
    private TaskAdapter taskAdapter;
    private AlertDialog.Builder builder;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTaskBinding = ActivityTaskBinding.inflate(getLayoutInflater());
        View view = activityTaskBinding.getRoot();
        setContentView(view);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setSupportActionBar(toolbar);

        //activityTaskBinding.toolbar.toolTitle.setText(getString(R.string.todoTitle));
        activityTaskBinding.recyclerView.setHasFixedSize(true);
        taskAdapter = new TaskAdapter(new TaskAdapter.WordDiff());
        activityTaskBinding.recyclerView.setAdapter(taskAdapter);
        activityTaskBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(this, android.R.color.background_dark));
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        activityTaskBinding.tvWorkThings.setPaintFlags(paint.getFlags());

        activityTaskBinding.tvWorkThings.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        builder = new AlertDialog.Builder(this);

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(TaskActivity.this, EditTaskActivity.class);
                intent.putExtra("id", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("status", task.getStatus());
                intent.putExtra("priority", task.getPriority());
                startActivity(intent);
            }
        });

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);


        taskViewModel.getAllTasks().observe(this, tasks -> {
            taskAdapter.submitList(tasks);
            calculatePercentage(tasks);


        });

        taskViewModel.getAllTasksCompleted().observe(this, tasks -> {
            tasksCompleted = tasks.size();
        });

        taskAdapter.setLongClickListener(new TaskAdapter.OnLongClickListener() {
            @Override
            public void OnLongClick(Task task) {
                builder.setTitle("Delete")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                taskViewModel.deleteTask(task);
                                Toast.makeText(TaskActivity.this, "Task item deleted.", Toast.LENGTH_SHORT).show();
                                taskAdapter.notifyDataSetChanged();

                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        taskAdapter.setCompleteTaskListener(new TaskAdapter.onCompleteTaskListener() {
            @Override
            public void onItemClick(Task task) {
                task.setStatus("Yes");
                taskViewModel.updateTask(task);
                taskAdapter.notifyDataSetChanged();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                builder.setTitle("Delete")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                taskViewModel.deleteTask(taskAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(TaskActivity.this, "Task item deleted.", Toast.LENGTH_SHORT).show();
                                taskAdapter.notifyDataSetChanged();

                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        }).attachToRecyclerView(activityTaskBinding.recyclerView);

        activityTaskBinding.btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(activityTaskBinding.txtAddTask.getText())) {
                    activityTaskBinding.txtAddTask.setError("Please enter task item.");
                    activityTaskBinding.txtAddTask.requestFocus();
                } else {
                    String taskItem = activityTaskBinding.txtAddTask.getText().toString();
                    Task task = new Task();
                    task.setTitle(taskItem);
                    task.setDescription("New Task Description");
                    task.setStatus("No");
                    task.setPriority(0);
                    taskViewModel.addTask(task);
                    taskAdapter.notifyDataSetChanged();

                }

            }
        });


        findViewById(R.id.tvMyHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(TaskActivity.this,TaskActivity.class);
               startActivity(intent);
               finish();
            }
        });

        findViewById(R.id.tvMyAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskActivity.this, "You clicked my account", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tvLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskActivity.this, "You clicked sign out", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void calculatePercentage(List<Task> tasks) {
        int completedTask = 0;
        for (Task task : tasks) {
            if (task.getStatus().equals("Yes") && !task.getStatus().equals("")) {
                completedTask++;
            }
        }

        int percentage;
        if (tasks.size() > 0) {
            percentage = Integer.parseInt(String.valueOf(completedTask * 100 / tasks.size()));
        } else {
            percentage = 0;
        }

        activityTaskBinding.progressBarPercentage.setProgress(percentage);
        activityTaskBinding.progressBarPercentage.setMax(100);
        activityTaskBinding.tvProgressPercentage.setText(percentage + getString(R.string.percentageSymbol));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (item != null && item.getItemId() == android.R.id.home) {
//            toggle();
//        }
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
            case R.id.menuDeleteAllTasks:
                LiveData<List<Task>> alltasks = taskViewModel.getAllTasks();

                if(alltasks.getValue().size() > 0){
                    taskViewModel.deleteAllTasks();
                    Toast.makeText(this, "All Task Items are deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "There are no tasks to be deleted.", Toast.LENGTH_SHORT).show();
                }

               /*
                 taskViewModel.getAllTasks().observe(this, tasks -> {
                     if(tasks.size() > 0){
                         taskViewModel.deleteAllTasks();
                         Toast.makeText(this, "All Task Items are deleted", Toast.LENGTH_SHORT).show();
                     }else{
                         Toast.makeText(this, "There are no tasks to be deleted.", Toast.LENGTH_SHORT).show();
                     }
                 });*/

                return true;
            case R.id.menuAbout:
                String url = "https://now.dstv.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuLogout:
                Toast.makeText(this, "You clicked logout", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggle() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

}