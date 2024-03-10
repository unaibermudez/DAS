package com.example.entrega_1.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega_1.EditTaskActivity;
import com.example.entrega_1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> taskList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        onEditClickListener = listener;
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskName;
        TextView textViewTaskDescription;
        TextView textViewDueDate;
        Button buttonEditTask;
        Button buttonDeleteTask;

        public TaskViewHolder(View itemView) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            buttonEditTask = itemView.findViewById(R.id.buttonEditTask);
            buttonDeleteTask = itemView.findViewById(R.id.buttonDeleteTask);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);

        holder.textViewTaskName.setText(currentTask.getName());
        holder.textViewTaskDescription.setText(currentTask.getDescription());

        // Format and display the due date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dueDate = dateFormat.parse(currentTask.getDueDate());
            String formattedDate = dateFormat.format(dueDate);
            holder.textViewDueDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textViewDueDate.setText("Invalid date format");
        }

        // Set click listeners for edit and delete buttons
        holder.buttonEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
                Intent intent = new Intent(v.getContext(), EditTaskActivity.class);
                intent.putExtra("task_id", currentTask.getId()); // Pass the task ID to the edit activity
                v.getContext().startActivity(intent);
            }
        });



        holder.buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
