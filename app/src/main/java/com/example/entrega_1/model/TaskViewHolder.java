package com.example.entrega_1.model;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega_1.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView textViewTaskName;
    TextView textViewTaskDescription;
    TextView textViewDueDate;

    public TaskViewHolder(View itemView) {
        super(itemView);
        textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
        textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
        textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
    }
}

