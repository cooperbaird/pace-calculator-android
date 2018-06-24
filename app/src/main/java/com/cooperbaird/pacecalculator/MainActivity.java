package com.cooperbaird.pacecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private EditText distanceText, splitText;
    private EditText timeHours, timeMinutes, timeSeconds;
    private EditText paceMinutes, paceSeconds;
    private ToggleButton distanceToggle, splitToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distanceText = findViewById(R.id.distance);
        splitText = findViewById(R.id.split);
        timeHours = findViewById(R.id.time_hours);
        timeMinutes = findViewById(R.id.time_minutes);
        timeSeconds = findViewById(R.id.time_seconds);

        distanceToggle = findViewById(R.id.distance_toggle);
        splitToggle = findViewById(R.id.split_toggle);

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = spinner.getSelectedItem().toString();

                if(selection.equals("3K")) {
                    distanceText.setText("3");
                    distanceToggle.setChecked(false);
                } else if(selection.equals("5K")) {
                    distanceText.setText("5");
                    distanceToggle.setChecked(false);
                } else if(selection.equals("8K")) {
                    distanceText.setText("8");
                    distanceToggle.setChecked(false);
                } else if(selection.equals("10K")) {
                    distanceText.setText("10");
                    distanceToggle.setChecked(false);
                } else if(selection.equals("Half Marathon")) {
                    distanceText.setText("13.109375");
                    distanceToggle.setChecked(true);
                } else if(selection.equals("Marathon")) {
                    distanceText.setText("26.21875");
                    distanceToggle.setChecked(true);
                } else {
                    distanceText.setText(null);
                    distanceToggle.setChecked(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Button calculateButton = findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
