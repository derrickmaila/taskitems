package com.derrick.dstvtodolist.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.derrick.dstvtodolist.R;
import com.derrick.dstvtodolist.models.Task;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {
    private OnItemClickListener listener;
    private OnLongClickListener longClickListener;
    private onCompleteTaskListener onCompleteTaskListener;
    public TaskAdapter(@NonNull DiffUtil.ItemCallback<Task> WordDiff_callback) {
        super(WordDiff_callback);
    }
    public static class WordDiff extends DiffUtil.ItemCallback<Task> {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask.getId() == newTask.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask.getTitle().equals(newTask.getTitle());
        }
    };

    @NonNull
    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position) {
        Task currentTask = getItem(position);
        holder.taskTitle.setText(currentTask.getTitle());
        holder.taskDescription.setText(currentTask.getDescription());

        if(currentTask.getStatus().equals("Yes")){
            holder.chkComplete.setChecked(true);
            holder.chkComplete.setEnabled(false);
            holder.taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskDescription.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.chkComplete.setChecked(false);
            holder.chkComplete.setEnabled(true);
        }
    }
    public Task getTaskAt(int position) {
        return getItem(position);
    }
    class TaskHolder extends RecyclerView.ViewHolder {
        private CheckBox chkComplete;
        private TextView taskDescription;
        private TextView taskTitle;
        public TaskHolder(View itemView) {
            super(itemView);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            chkComplete = itemView.findViewById(R.id.chkComplete);
            taskTitle = itemView.findViewById(R.id.taskTitle);

            chkComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCompleteTaskListener.onItemClick(getItem(getAdapterPosition()));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                        longClickListener.OnLongClick(getItem(position));
                    }
                    return true;
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Task task);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnLongClickListener{
        void OnLongClick(Task task);
    }
    public void setLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public  interface onCompleteTaskListener{
        void onItemClick(Task task);
    }

    public void setCompleteTaskListener(onCompleteTaskListener onCompleteTaskListener) {
        this.onCompleteTaskListener = onCompleteTaskListener;
    }

}