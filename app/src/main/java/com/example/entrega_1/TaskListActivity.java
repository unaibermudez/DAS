package com.example.entrega_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.entrega_1.model.Task;
import com.example.entrega_1.model.TaskAdapter;
import com.example.entrega_1.model.TaskDBHelper;

import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private TaskDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize TaskDBHelper
        dbHelper = new TaskDBHelper(this);

        // Fetch tasks from database
        taskList = dbHelper.getAllTasks();

        // Set up RecyclerView adapter
        adapter = new TaskAdapter(taskList);
        adapter.setOnDeleteClickListener(this); // Set the onDeleteClickListener
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onDeleteClick(int position) {
        // Get the task at the clicked position
        Task taskToDelete = taskList.get(position);
        // Delete the task from the database
        dbHelper.deleteTask(taskToDelete.getId());
        // Remove the task from the list and notify the adapter
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
    }

}
