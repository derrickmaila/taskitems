package com.derrick.dstvtodolist.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.derrick.dstvtodolist.R;
import com.derrick.dstvtodolist.databinding.ActivityEditTaskBinding;
import com.derrick.dstvtodolist.databinding.ActivityTaskBinding;
import com.derrick.dstvtodolist.models.Task;
import com.derrick.dstvtodolist.modelviews.TaskViewModel;

public class EditTaskActivity extends AppCompatActivity {
    public ActivityEditTaskBinding activityEditTaskBinding;
    private int priority;
    private String status;
    private Intent intent;
    RadioButton radioButtonStatus;
    RadioButton radioButtonPriority;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditTaskBinding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        View view = activityEditTaskBinding.getRoot();
        setContentView(view);
         intent = getIntent();
        activityEditTaskBinding.toolbar.toolTitle.setText(intent.getStringExtra("title"));

        activityEditTaskBinding.txtTaskTitle.setText(intent.getStringExtra("title"));
        activityEditTaskBinding.txtTaskDescription.setText(intent.getStringExtra("description"));

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

       status =  intent.getStringExtra("status");
       CheckStatus(status);

        priority = intent.getIntExtra("priority", 0);
        Log.e("RADIOBUTTONTEST","CHECKED BUTTON :  priority : " +priority );
        CheckPriority(priority);

        activityEditTaskBinding.btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean proceed = validForm();
                if(proceed){
                    String title = activityEditTaskBinding.txtTaskTitle.getText().toString();
                    String description = activityEditTaskBinding.txtTaskDescription.getText().toString();

                    radioButtonStatus =  findViewById(activityEditTaskBinding.idRadioGroupStatus.getCheckedRadioButtonId());
                    String status = radioButtonStatus.getText().toString();

                    radioButtonPriority = findViewById(activityEditTaskBinding.idRadioGroupPriority.getCheckedRadioButtonId());
                    String priorityValue = radioButtonPriority.getText().toString();
                    int priority  = CreatePriority(priorityValue);

                    Task task = new Task();
                    task.setId(intent.getIntExtra("id",0));
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setStatus(status);
                    task.setPriority(priority);
                    taskViewModel.updateTask(task);
                    Log.e("RADIOBUTTONTEST","CHECKED BUTTON :  title : " +title + "\n description : "
                            + description  + "\n status : " + status + "\n priority : " + priority
                            + "\n ID : " + intent.getIntExtra("id",0));

                    Intent intentActivity = new Intent(EditTaskActivity.this,TaskActivity.class);
                    startActivity(intentActivity);
                    finish();

                }
            }
        });

    }

    private int CreatePriority(String priorityValue) {
        int priority;
        switch (priorityValue){
            case "Low":
                priority = 0;
                break;
            case "Medium":
                priority = 1;
                break;
            case "High":
                priority = 2;
                break;
            default:
                priority = 0;
                break;

        }

        return priority;

    }

    private void CheckPriority(int priority) {
        switch (priority) {
            case 0:
                activityEditTaskBinding.idLow.setChecked(true);
                break;
            case 1:
                activityEditTaskBinding.idMedium.setChecked(true);
                break;
            case 2:
                activityEditTaskBinding.idHigh.setChecked(true);
                break;
        }
    }

    private void CheckStatus(String status) {
        switch (status) {
            case "Yes":
                activityEditTaskBinding.idYes.setChecked(true);
                break;
            case "No":
                activityEditTaskBinding.idNo.setChecked(true);
                break;
        }
    }

    private boolean validForm() {
        boolean proceed = true;
        if (TextUtils.isEmpty(activityEditTaskBinding.txtTaskTitle.getText())) {
            activityEditTaskBinding.txtTaskTitle.setError("Please enter title.");
            activityEditTaskBinding.txtTaskTitle.requestFocus();
            proceed = false;
        }

        if (TextUtils.isEmpty(activityEditTaskBinding.txtTaskDescription.getText())) {
            activityEditTaskBinding.txtTaskDescription.setError("Please enter description.");
            activityEditTaskBinding.txtTaskDescription.requestFocus();
            proceed = false;
        }

        return proceed;
    }
}