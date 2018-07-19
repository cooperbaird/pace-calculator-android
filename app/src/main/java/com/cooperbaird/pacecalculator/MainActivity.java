package com.cooperbaird.pacecalculator;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText distanceText, splitText;
    private EditText timeHours, timeMinutes, timeSeconds;
    private EditText paceMinutes, paceSeconds;
    private ToggleButton distanceToggle, splitToggle;
    private RecyclerView mSplitRecyclerView;
    private SplitAdapter mAdapter;
    private List<Split> mSplitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distanceText = findViewById(R.id.distance);
        splitText = findViewById(R.id.split);
        timeHours = findViewById(R.id.time_hours);
        timeMinutes = findViewById(R.id.time_minutes);
        timeSeconds = findViewById(R.id.time_seconds);
        paceMinutes = findViewById(R.id.pace_minutes);
        paceSeconds = findViewById(R.id.pace_seconds);

        distanceToggle = findViewById(R.id.distance_toggle);
        splitToggle = findViewById(R.id.split_toggle);

        mSplitRecyclerView = findViewById(R.id.split_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSplitRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mSplitRecyclerView.getContext(), layoutManager.getOrientation());
        mSplitRecyclerView.addItemDecoration(dividerItemDecoration);

        updateUI();

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = spinner.getSelectedItem().toString();
                switch(selection) {
                    case "3K":
                        distanceText.setText("3");
                        distanceToggle.setChecked(false);
                        break;
                    case "5K":
                        distanceText.setText("5");
                        distanceToggle.setChecked(false);
                        break;
                    case "8K":
                        distanceText.setText("8");
                        distanceToggle.setChecked(false);
                        break;
                    case "10K":
                        distanceText.setText("10");
                        distanceToggle.setChecked(false);
                        break;
                    case "Half Marathon":
                        distanceText.setText("13.109375");
                        distanceToggle.setChecked(true);
                        break;
                    case "Marathon":
                        distanceText.setText("26.21875");
                        distanceToggle.setChecked(true);
                        break;
                    default:
                        distanceText.setText(null);
                        distanceToggle.setChecked(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Button calculateButton = findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    calculate();
                } catch(Exception ex) {
                    Toast.makeText(MainActivity.this, "Improper Values", Toast.LENGTH_SHORT).show();
                }
                updateUI();
            }
        });
    }

    private void calculate() {
        mSplitList.clear();
        String d = distanceText.getText().toString().trim();
        String tH = timeHours.getText().toString().trim();
        String tM = timeMinutes.getText().toString().trim();
        String tS = timeSeconds.getText().toString().trim();
        String pM = paceMinutes.getText().toString().trim();
        String pS = paceSeconds.getText().toString().trim();
        String split = splitText.getText().toString().trim();

        char units = 'm';
        if(!distanceToggle.isChecked())
            units = 'k';

        char sUnits = 'm';
        if(!splitToggle.isChecked())
            sUnits = 'k';

        if(!d.equals("") && (!tH.equals("") || !tM.equals("") || !tS.equals(""))) {
            calcPace(d, tH, tM, tS, units);
        } else if(!d.equals("") && (!pM.equals("") || !pS.equals(""))) {
            calcTime(d, pM, pS, units);
        } else if((!tH.equals("") || !tM.equals("") || !tS.equals("")) &&
                (!pM.equals("") || !pS.equals("")) && d.equals("")) {
            calcDistance(tH, tM, tS, pM, pS, units);
        }

        if(!split.equals("")) {
            calcSplits(d, tH, tM, tS, split, units, sUnits);
        }
    }

    private void calcSplits(String d, String tH, String tM, String tS, String split, char units, char sUnits) {
        double distance = Double.parseDouble(d);
        double s = Double.parseDouble(split);
        Time time = parseTime(tH, tM, tS);
        mSplitList = RunningCalculations.calculateSplits(units, sUnits, distance, s, time);
    }

    private void calcDistance(String tH, String tM, String tS, String pM, String pS, char units) {
        Time time = parseTime(tH, tM, tS);
        Pace pace = new Pace(Integer.parseInt(pM), Double.parseDouble(pS));
        double distance = RunningCalculations.calculateDistance(units, pace, time);
        distance = RunningCalculations.roundToDecimal(distance, 3);
        distanceText.setText(Double.toString(distance));
    }

    private void calcTime(String d, String pM, String pS, char units) {
        Pace pace = new Pace(Integer.parseInt(pM), Double.parseDouble(pS));
        Time time = RunningCalculations.calculateTime(units, Double.parseDouble(d), pace);

        timeHours.setText(time.getHoursAsString());

        String strMinutes = time.getMinutesAsString();
        if(time.getMinutes() < 10 && time.getHours() > 0)
            strMinutes = "0" + strMinutes;
        timeMinutes.setText(strMinutes);

        String strSecs = getStringSeconds(time);
        timeSeconds.setText(strSecs);
    }

    private void calcPace(String d, String tH, String tM, String tS, char units) {
        Time time = parseTime(tH, tM, tS);
        Pace pace = RunningCalculations.calculatePace(units, Double.parseDouble(d), time);
        paceMinutes.setText(pace.getMinutesAsString());
        String strSecs = getStringSeconds(pace);
        paceSeconds.setText(strSecs);
    }

    private Time parseTime(String hours, String minutes, String seconds) {
        int hrs;
        if(hours.equals(""))
            hrs = 0;
        else
            hrs = Integer.parseInt(hours);

        int min;
        if(minutes.equals(""))
            min = 0;
        else
            min = Integer.parseInt(minutes);

        double sec;
        if(seconds.equals(""))
            sec = 0;
        else
            sec = Double.parseDouble(seconds);

        return new Time(hrs, min, sec);
    }

    private String getStringSeconds(AbstractTime t) {
        double roundedSecs = t.getRoundedSeconds(3);
        String strSecs = Double.toString(roundedSecs);
        if(roundedSecs < 10)
            strSecs = "0" + strSecs;
        return strSecs;
    }

    private void updateUI() {
        if(mAdapter == null) {
            mAdapter = new SplitAdapter(mSplitList);
            mSplitRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSplits(mSplitList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class SplitHolder extends RecyclerView.ViewHolder {
        private TextView mSplitLap, mSplitTime;
        private Split mSplit;

        public SplitHolder(LayoutInflater inflater, ViewGroup parent, int layout) {
            super(inflater.inflate(layout, parent, false));
            mSplitLap = itemView.findViewById(R.id.split_lap);
            mSplitTime = itemView.findViewById(R.id.split_time);
        }

        public void bind(Split split) {
            mSplit = split;
            mSplitLap.setText(mSplit.getLapAsString());
            mSplitTime.setText(mSplit.toString());
        }
    }

    private class SplitAdapter extends RecyclerView.Adapter<SplitHolder> {
        private List<Split> mSplits;

        public SplitAdapter(List<Split> splits) {
            mSplits = splits;
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.split_item;
        }

        @NonNull
        @Override
        public SplitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SplitHolder(inflater, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SplitHolder holder, int position) {
            Split split = mSplits.get(position);
            holder.bind(split);
        }

        @Override
        public int getItemCount() {
            return mSplits.size();
        }

        public void setSplits(List<Split> splits) {
            mSplits = splits;
        }
    }
}
