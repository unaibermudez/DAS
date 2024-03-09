package com.example.entrega_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import com.example.entrega_1.model.TaskDBHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //test commit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        EditText editTextTaskName = findViewById(R.id.editTextTaskName);
        EditText editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        EditText editTextDueDate = findViewById(R.id.editTextDueDate);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        Button buttonShowTasks = findViewById(R.id.buttonShowTasks);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add Task button clicked
                // Retrieve input data
                String taskName = editTextTaskName.getText().toString();
                String taskDescription = editTextTaskDescription.getText().toString();
                String dueDate = editTextDueDate.getText().toString();

                // Insert data into database
                TaskDBHelper dbHelper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("name", taskName);
                values.put("description", taskDescription);
                values.put("due_date", dueDate);

                long newRowId = db.insert("tasks", null, values);

                // Display toast message indicating success or failure
                if (newRowId != -1) {
                    Toast.makeText(MainActivity.this, "Task added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add task.", Toast.LENGTH_SHORT).show();
                }

                // Clear input fields after adding task
                editTextTaskName.getText().clear();
                editTextTaskDescription.getText().clear();
                editTextDueDate.getText().clear();
            }
        });

        buttonShowTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Tasks button clicked
                try {
                    Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to start TaskListActivity", e);
                }
            }
        });

    }
}