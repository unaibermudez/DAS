package com.example.entrega_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.example.entrega_1.model.Task;
import com.example.entrega_1.model.TaskDBHelper;

import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTextTaskName;
    private EditText editTextTaskDescription;
    private EditText editTextDueDate;
    private Button buttonSaveTask;
    private Button buttonCancel;

    private TaskDBHelper dbHelper;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Initialize views
        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Initialize TaskDBHelper
        dbHelper = new TaskDBHelper(this);

        // Get task ID from intent extras
        int taskId = getIntent().getIntExtra("task_id", -1);

        // Retrieve task details from the database
        task = dbHelper.getTask(taskId);

        // Set retrieved task details to EditText fields
        editTextTaskName.setText(task.getName());
        editTextTaskDescription.setText(task.getDescription());
        editTextDueDate.setText(task.getDueDate());

        // Set click listeners for Save and Cancel buttons
        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated task details from EditText fields
                String updatedTaskName = editTextTaskName.getText().toString().trim();
                String updatedTaskDescription = editTextTaskDescription.getText().toString().trim();
                String updatedDueDate = editTextDueDate.getText().toString().trim();

                // Update the task in the database with the new details
                task.setName(updatedTaskName);
                task.setDescription(updatedTaskDescription);
                task.setDueDate(updatedDueDate);
                dbHelper.updateTask(task);

                // Finish the activity
                // create an intent to go back to the TaskListActivity
                Intent intent = new Intent(EditTaskActivity.this, TaskListActivity.class);
                startActivity(intent);

            }
        });

        // Set click listener for the due date EditText to show date picker dialog
        editTextDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement cancel button logic
                finish(); // Close the activity
            }
        });
    }

    // Define the method to show date picker dialog
    private void showDatePickerDialog() {
        // Implement the date picker dialog logic here
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the due date EditText
                        editTextDueDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}