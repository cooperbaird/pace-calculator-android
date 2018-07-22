package com.cooperbaird.pacecalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    private EditText mDistanceText, mSplitText;
    private EditText mTimeHours, mTimeMinutes, mTimeSeconds;
    private EditText mPaceMinutes, mPaceSeconds;
    private ToggleButton mDistanceToggle, mSplitToggle;
    private Spinner spinner;
    private RecyclerView mSplitRecyclerView;
    private SplitAdapter mAdapter;
    private List<Split> mSplitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDistanceText = findViewById(R.id.distance);
        mSplitText = findViewById(R.id.split);
        mTimeHours = findViewById(R.id.time_hours);
        mTimeMinutes = findViewById(R.id.time_minutes);
        mTimeSeconds = findViewById(R.id.time_seconds);
        mPaceMinutes = findViewById(R.id.pace_minutes);
        mPaceSeconds = findViewById(R.id.pace_seconds);

        mDistanceToggle = findViewById(R.id.distance_toggle);
        mSplitToggle = findViewById(R.id.split_toggle);

        mSplitRecyclerView = findViewById(R.id.split_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSplitRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mSplitRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mSplitRecyclerView.addItemDecoration(dividerItemDecoration);

        updateUI();

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = spinner.getSelectedItem().toString();
                switch(selection) {
                    case "3K":
                        mDistanceText.setText("3");
                        mDistanceToggle.setChecked(false);
                        break;
                    case "5K":
                        mDistanceText.setText("5");
                        mDistanceToggle.setChecked(false);
                        break;
                    case "8K":
                        mDistanceText.setText("8");
                        mDistanceToggle.setChecked(false);
                        break;
                    case "10K":
                        mDistanceText.setText("10");
                        mDistanceToggle.setChecked(false);
                        break;
                    case "Half Marathon":
                        mDistanceText.setText("13.109375");
                        mDistanceToggle.setChecked(true);
                        break;
                    case "Marathon":
                        mDistanceText.setText("26.21875");
                        mDistanceToggle.setChecked(true);
                        break;
                    default:
                        mDistanceText.setText(null);
                        mDistanceToggle.setChecked(true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.reset:
                reset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reset() {
        mDistanceText.setText("");
        mSplitText.setText("");
        mTimeHours.setText("");
        mTimeMinutes.setText("");
        mTimeSeconds.setText("");
        mPaceMinutes.setText("");
        mPaceSeconds.setText("");
        spinner.setSelection(0);
        mDistanceToggle.setChecked(true);
        mSplitToggle.setChecked(true);
        mSplitList.clear();
        updateUI();
    }

    private void calculate() {
        // shift focus to main activity and hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        findViewById(R.id.mainLayout).requestFocus();

        mSplitList.clear();

        String d = mDistanceText.getText().toString();
        String tH = mTimeHours.getText().toString();
        String tM = mTimeMinutes.getText().toString();
        String tS = mTimeSeconds.getText().toString();
        String pM = mPaceMinutes.getText().toString();
        String pS = mPaceSeconds.getText().toString();
        String split = mSplitText.getText().toString();

        char units = 'm';
        if(!mDistanceToggle.isChecked())
            units = 'k';

        char sUnits = 'm';
        if(!mSplitToggle.isChecked())
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
            calcSplits(mDistanceText.getText().toString(), mTimeHours.getText().toString(),
                    mTimeMinutes.getText().toString(), mTimeSeconds.getText().toString(), split, units, sUnits);
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
        Pace pace = parsePace(pM, pS);
        double distance = RunningCalculations.calculateDistance(units, pace, time);
        distance = RunningCalculations.roundToDecimal(distance, 3);
        mDistanceText.setText(Double.toString(distance));
    }

    private void calcTime(String d, String pM, String pS, char units) {
        Pace pace = parsePace(pM, pS);
        Time time = RunningCalculations.calculateTime(units, Double.parseDouble(d), pace);

        mTimeHours.setText(time.getHoursAsString());

        String strMinutes = time.getMinutesAsString();
        if(time.getMinutes() < 10 && time.getHours() > 0)
            strMinutes = "0" + strMinutes;
        mTimeMinutes.setText(strMinutes);

        String strSecs = getStringSeconds(time);
        mTimeSeconds.setText(strSecs);
    }

    private void calcPace(String d, String tH, String tM, String tS, char units) {
        Time time = parseTime(tH, tM, tS);
        Pace pace = RunningCalculations.calculatePace(units, Double.parseDouble(d), time);
        mPaceMinutes.setText(pace.getMinutesAsString());
        String strSecs = getStringSeconds(pace);
        mPaceSeconds.setText(strSecs);
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

    private Pace parsePace(String minutes, String seconds) {
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

        return new Pace(min, sec);
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
