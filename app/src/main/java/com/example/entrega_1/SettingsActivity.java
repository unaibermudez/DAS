package com.example.entrega_1;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import java.util.Locale;

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

        // Initialize language spinner
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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                String currentLanguage = preferences.getString("selected_language", "");

                // Check if the selected language is different from the current language
                if (!selectedLanguage.equals(getLanguageName(currentLanguage))) {
                    setLocale(selectedLanguage);
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Initialize dark mode switch
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch darkModeSwitch = findViewById(R.id.switchDarkMode);
        boolean isDarkModeEnabled = preferences.getBoolean("dark_mode_enabled", true);
        darkModeSwitch.setChecked(isDarkModeEnabled);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkMode(isChecked);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

        // Set the new locale configuration
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(newLocale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // Save the selected language in SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selected_language", newLocale.getLanguage());
        editor.apply();
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

    private void setDarkMode(boolean isEnabled) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean("dark_mode_enabled", isEnabled);
    editor.apply();

    // Apply dark mode
    int nightModeFlag = isEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
    AppCompatDelegate.setDefaultNightMode(nightModeFlag);

    // Recreate the activity to apply changes
    recreate();

    // Show toast notification for dark mode change
    String toastMessage = getString(isEnabled ? R.string.dark_mode_enabled_toast : R.string.dark_mode_disabled_toast);
    Toast.makeText(SettingsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
}
}
