package com.example.entrega_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    // Define a request code to identify the settings activity
    private static final int SETTINGS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the saved language from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");

        // Set the saved language
        if (!selectedLanguage.isEmpty()) {
            setLocale(selectedLanguage);
        }

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

        // Step 1: Create Notification Channel
        createNotificationChannel();

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
                    int toastMessageResId = R.string.task_name_due_date_required;
                    String toastMessage = getString(toastMessageResId);

                    Toast toast = Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT);
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
                        int toastMessageResId = R.string.task_added_successfully;
                        String toastMessage = getString(toastMessageResId);
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        int toastMessageResId = R.string.task_added_failed;
                        String toastMessage = getString(toastMessageResId);
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    }

                    // Step 2: Display Notification
                    displayNotification();

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

    // Step 1: Create Notification Channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Step 2: Display Notification
    private void displayNotification() {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int toastMessageResId = R.string.task_added_notification;
        String icon = getString(toastMessageResId);
        int toastMessageResId2 = R.string.task_added_notification_text;
        String title = getString(toastMessageResId2);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(icon)
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
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
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app: https://github.com/unaibermudez/DAS_Entrega1");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            // Handle settings button click
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Define a method to handle the result when returning from SettingsActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the activity to apply language changes
            recreate();
        }
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
