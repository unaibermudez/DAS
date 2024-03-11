package com.example.entrega_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner languageSpinner = findViewById(R.id.spinnerLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set the current language as the selected item in the spinner
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        int position = adapter.getPosition(getLanguageName(selectedLanguage));
        languageSpinner.setSelection(position);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = parentView.getItemAtPosition(position).toString();
                setLocale(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Navigate to the parent activity (MainActivity)
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLocale(String language) {
        Locale newLocale;
        switch (language) {
            case "Spanish":
                newLocale = new Locale("es");
                break;
            case "Basque":
                newLocale = new Locale("eu");
                break;
            default:
                newLocale = Locale.ENGLISH;
                break;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selected_language", newLocale.toString());
        editor.apply();

        // Check if the selected language is different from the current locale
        if (!newLocale.equals(getResources().getConfiguration().locale)) {
            // Update the locale settings
            Locale.setDefault(newLocale);
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            configuration.setLocale(newLocale);
            resources.updateConfiguration(configuration, displayMetrics);

            // Recreate the activity to apply language changes
            recreate();

            // Show toast notification for language change
            String toastMessage = getString(R.string.language_changed_toast);
            Toast.makeText(SettingsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private String getLanguageName(String languageCode) {
        switch (languageCode) {
            case "es":
                return "Spanish";
            case "eu":
                return "Basque";
            default:
                return "English";
        }
    }
}
