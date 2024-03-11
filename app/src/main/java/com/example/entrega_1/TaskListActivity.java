package com.example.entrega_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.entrega_1.model.Task;
import com.example.entrega_1.model.TaskAdapter;
import com.example.entrega_1.model.TaskDBHelper;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnDeleteClickListener, TaskAdapter.OnEditClickListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private TaskDBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
        adapter.setOnEditClickListener(this);   // Set the onEditClickListener
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

    @Override
    public void onEditClick(int position) {
        // Get the task at the clicked position
        Task taskToEdit = taskList.get(position);

        // Start EditTaskActivity with the selected task
        Intent intent = new Intent(TaskListActivity.this, EditTaskActivity.class);
        intent.putExtra("TASK_ID", taskToEdit.getId()); // Pass the task ID to the edit activity
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Navigate to the parent activity (MainActivity)
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
