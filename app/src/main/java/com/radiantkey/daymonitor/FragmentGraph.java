package com.radiantkey.daymonitor;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FragmentGraph extends Fragment {
    PieChart pieChart;

    DatabaseHelper mdb;
    HashMap<String, HashMap<String, Long>> mList;

    Spinner order, length;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        order = view.findViewById(R.id.order);
        length = view.findViewById(R.id.length);

        ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.order));
        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.length));
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order.setAdapter(orderAdapter);
        length.setAdapter(lengthAdapter);

        //TODO add functionality to spinner to correctly generate graph
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                processChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        length.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                processChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        drawPieChart();
    }

    private void drawPieChart(){
        mdb = new DatabaseHelper(getContext());

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.99f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


    }

    public void processChart(){
        //generate pie value from history table example
//        value.add(new PieEntry(34f, "PartyA"));

        mList = new HashMap<String, HashMap<String, Long>>();
        processHistory();
        ArrayList<PieEntry>value = setChart();

        Description description = new Description();
        description.setText("something");
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseOutCubic);

        PieDataSet dataSet = new PieDataSet(value, "Countries");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
    }

    public void processHistory(){
        Cursor res;
        long start = processTime();
        if(start > 0)
            res = mdb.getPortionH(start);
        else
            res = mdb.getAllDataH();
        if(res.getCount() == 0){
            //Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
            showMessage("Error", "No data found");
            return;
        }else{
//            HISTORY database column: ID, NAME, CATEGORY, START_TIME, LENGTH
            while(res.moveToNext()){
                if(mList.get(res.getString(2)) == null) {
                    HashMap<String, Long> innerMap = new HashMap<>();
                    innerMap.put(res.getString(1), res.getLong(4));
                    mList.put(res.getString(2), innerMap);
                }else {
                    Map<String, Long> innerMap = mList.get(res.getString(2));
                    innerMap.put(res.getString(1), innerMap.get(res.getString(1)) + res.getLong(4));
                }
            }
        }
    }

    //uses spinner length position
    public long processTime(){
        long end = Calendar.getInstance().getTimeInMillis();
        long start = 0;
        switch (length.getSelectedItemPosition()){
            case 0://day
                start = end - TimeUnit.DAYS.toMillis(1);
                break;
            case 1://week
                start = end - TimeUnit.DAYS.toMillis(7);
                break;
            case 2://month
                start = end - TimeUnit.DAYS.toMillis(30);
                break;
            case 3://year
                start = end - TimeUnit.DAYS.toMillis(365);
                break;
            case 4://whole
                start = 0;
                break;
        }
        return start;
    }

    //uses spinner order position
    public ArrayList<PieEntry> setChart(){
        ArrayList<PieEntry> value = new ArrayList<>();
        switch (order.getSelectedItemPosition()) {
            case 0:
                for (Map.Entry<String, HashMap<String, Long>> pair : mList.entrySet()) {
                    for (Map.Entry<String, Long> innerPair : pair.getValue().entrySet()) {
                        value.add(new PieEntry(innerPair.getValue(), innerPair.getKey()));
                    }
                }
                break;
            case 1:
                for (Map.Entry<String, HashMap<String, Long>> pair : mList.entrySet()) {
                    long time = 0;
                    for (Map.Entry<String, Long> innerPair : pair.getValue().entrySet()) {
                        time += innerPair.getValue();
                    }
                    value.add(new PieEntry(time, pair.getKey()));
                }
                break;
        }
        return value;
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
