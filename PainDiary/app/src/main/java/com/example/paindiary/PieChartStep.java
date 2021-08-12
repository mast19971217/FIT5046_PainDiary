package com.example.paindiary;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiary.databinding.FragmentPieChartStepBinding;
import com.example.paindiary.room.PainRecord;

import com.example.paindiary.room.PainRecordViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PieChartStep extends Fragment {
    private FragmentPieChartStepBinding Binding;
    private FirebaseAuth mAuth;
    private Description description;
    private PainRecordViewModel painRecordViewModel;
    private String email;
    private static ArrayList<Integer> colors;
    private PieChart mChart;
    public PieChartStep(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = FragmentPieChartStepBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail();
        description=new Description();
        description.setText("Pain Location Pie Chart");
        mChart=Binding.pieChart;
        colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        painRecordViewModel  = new
                ViewModelProvider(this).get(PainRecordViewModel.class);
        painRecordViewModel.initalizeVars(getActivity().getApplication());
        painRecordViewModel.findByDate(new Date(System.currentTimeMillis()), mAuth.getCurrentUser().getEmail(), new CallBack<PainRecord>() {
            @Override
            public void onSucess(PainRecord painRecord) {
                List<PieEntry> entries = new ArrayList<>();
            if(painRecord==null)
            {
                Looper.prepare();
                ToastUtil.newToast(getActivity(),"no data!");
                Looper.loop();
            }
            else
                {
                    entries.add(new PieEntry(painRecord.steps,"Current Steps"));
                    if(painRecord.stepsaim<painRecord.steps){
                        entries.add(new PieEntry(0,"Remaining Steps"));
                    }
                    else {
                        entries.add(new PieEntry(painRecord.stepsaim-painRecord.steps,"Remaining Steps"));
                    }
            }
                PieDataSet set = new PieDataSet(entries,"");
                set.setColors(colors);
                PieData data = new PieData(set);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPieChartLocation(mChart,data);
                    }
                });
            }

            @Override
            public void onFailure() {
                Looper.prepare();
                ToastUtil.newToast(getActivity(),"fail to load data!");
                Looper.loop();
            }
        });
            return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Binding = null;
    }

    private void showPieChartLocation(PieChart pieChart, PieData pieData) {
        pieChart.setHoleRadius(30) ;
        pieChart.setDescription(description);

        pieChart.setRotationAngle(90);
        pieChart.setEntryLabelTextSize(8f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(false);

        pieChart.setExtraOffsets(30, 30, 30, 30);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(8f);
        pieChart.animateXY(1000, 1000);


        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method for show integer
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        };
        pieData.setValueFormatter(vf);

        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);




        pieChart.invalidate(); // refresh
    }
}