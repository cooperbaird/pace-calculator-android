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

        distanceToggle = findViewById(R.id.distance_toggle);
        splitToggle = findViewById(R.id.split_toggle);

        mSplitRecyclerView = findViewById(R.id.split_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParent());
        mSplitRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mSplitRecyclerView.getContext(), layoutManager.getOrientation());
        mSplitRecyclerView.addItemDecoration(dividerItemDecoration);

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
                updateUI();
            }
        });
    }

    /*@Override
    public void onResume() {
        super.onResume();
        updateUI();
    }*/

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
            mSplitLap = findViewById(R.id.split_lap);
            mSplitTime = findViewById(R.id.split_time);
        }

        public void bind(Split split) {
            mSplit = split;
            mSplitLap.setText(mSplit.getLap());
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
            LayoutInflater inflater = LayoutInflater.from(getParent());
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
