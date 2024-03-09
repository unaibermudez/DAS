package com.example.entrega_1.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.entrega_1.R;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> taskList;
    private OnDeleteClickListener onDeleteClickListener;

    // Constructor to initialize the taskList
    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    // Interface to handle delete button clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    // Method to set the delete button click listener
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    // ViewHolder class to hold the views for each task item
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskName;
        TextView textViewTaskDescription;
        TextView textViewDueDate;
        Button buttonDeleteTask;

        public TaskViewHolder(View itemView, final OnDeleteClickListener listener) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            buttonDeleteTask = itemView.findViewById(R.id.buttonDeleteTask);

            // Set click listener for delete button
            buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(view, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);
        holder.textViewTaskName.setText(currentTask.getName());
        holder.textViewTaskDescription.setText(currentTask.getDescription());
        holder.textViewDueDate.setText(currentTask.getDueDate());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
