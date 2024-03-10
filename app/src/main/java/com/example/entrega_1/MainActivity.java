package com.example.entrega_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.entrega_1.model.TaskDBHelper;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Declaring EditText as class-level variables
    EditText editTextTaskName;
    EditText editTextTaskDescription;
    EditText editTextDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        Button buttonShowTasks = findViewById(R.id.buttonShowTasks);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input data
                String taskName = editTextTaskName.getText().toString().trim();
                String taskDescription = editTextTaskDescription.getText().toString().trim();
                String dueDate = editTextDueDate.getText().toString().trim();

                // Check if the task name and due date are empty
                if (taskName.isEmpty() || dueDate.isEmpty()) {
                    // Show error message
                    Toast toast = Toast.makeText(MainActivity.this, "Task name and due date are required.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0); // Set the position to top center
                    toast.show();

                } else {
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

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

    // Define the method to show date picker dialog outside of onClickListener
    public void showDatePickerDialog(View v) {
        // Implement the date picker dialog logic here
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the due date EditText
                        editTextDueDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            // Handle share button click
            // Example: Share app link
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app: https://example.com");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
