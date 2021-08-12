package com.example.paindiary;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiary.databinding.FragmentLineChartBinding;
import com.example.paindiary.room.PainRecord;
import com.example.paindiary.room.PainRecordViewModel;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LineChart extends Fragment {
    private FragmentLineChartBinding Binding;
    private FirebaseAuth mAuth;
    private PainRecordViewModel painRecordViewModel;
    private Date starttime;
    private Date endtime;
    private SimpleDateFormat sdf1;
    private SimpleDateFormat sdf2;
    private CombinedChart chart;
    private Description description;

    public LineChart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf2 = new SimpleDateFormat("MM-dd");
        mAuth = FirebaseAuth.getInstance();
        Binding = FragmentLineChartBinding.inflate(inflater, container, false);

        chart = Binding.combinedChart;
        description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(true);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        View view = Binding.getRoot();
        painRecordViewModel = new
                ViewModelProvider(this).get(PainRecordViewModel.class);
        painRecordViewModel.initalizeVars(getActivity().getApplication());

        Binding.StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();//get current date
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String t = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                Binding.StartTime.setText(t);
                                try {
                                    starttime = sdf1.parse(t);
                                    //Log.d("tag",starttime.getMonth()+"");
                                } catch (Exception e) {
                                    Log.d("tag", e.getMessage());
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Binding.EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();//get current date
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String t = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                Binding.EndTime.setText(t);
                                try {
                                    endtime = sdf1.parse(t);
                                } catch (Exception e) {
                                    Log.d("tag", e.getMessage());
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Binding.draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starttime.after(endtime)) {
                    ToastUtil.newToast(getActivity(), "Please Set Correct Time Range!");
                    return;
                }
                painRecordViewModel.findBydaterange(mAuth.getCurrentUser().getEmail(), starttime, endtime, new CallBack<List<PainRecord>>() {
                    @Override
                    public void onSucess(List<PainRecord> painRecord) {

                        //Log.d("tag",painRecord.get(0).painlocation);
                        int t=Binding.spinner.getSelectedItemPosition();
                        generateLineData(chart, painRecord, t, Color.RED, Color.BLUE);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chart.notifyDataSetChanged();
                                chart.invalidate();
                                testCorrelation(painRecord,t);
                            }
                        });
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Binding = null;
    }

    private LineData generateLineData(CombinedChart chart, List<PainRecord> painRecords, int Index, int colorleft, int colorright) {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entriesright = new ArrayList<>();
        String rightlabel = "";
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        //  leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        // leftAxis.setAxisMaximum(10F);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        //xAxis.setAxisMinimum(0f);
        // xAxis.setAxisMaximum(31f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                String t = sdf2.format(value);
                return t;
            }
        });
        xAxis.setGranularity(1f);
        if (Index == 0) {
            for (int index = 0; index < painRecords.size(); index++)
                entriesright.add(new Entry(painRecords.get(index).date.getTime(), painRecords.get(index).temp));
            rightlabel = "Temperature(°C)";
            // rightAxis.setAxisMinimum(-40f); // this replaces setStartAtZero(true)
            // rightAxis.setAxisMaximum(40F);

        } else if (Index == 1) {
            for (int index = 0; index < painRecords.size(); index++)
                entriesright.add(new Entry(painRecords.get(index).date.getTime(), painRecords.get(index).humidity));
            rightlabel = "Humidity(%)";
            //rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            // rightAxis.setAxisMaximum(100F);
        } else if (Index == 2) {
            for (int index = 0; index < painRecords.size(); index++)
                entriesright.add(new Entry(painRecords.get(index).date.getTime(), painRecords.get(index).pressure));
            rightlabel = "Atmospheric Pressure (hPa)";
            // rightAxis.setAxisMinimum(900f); // this replaces setStartAtZero(true)
            //rightAxis.setAxisMaximum(1100F);
        }
        for (int index = 0; index < painRecords.size(); index++)
            entries.add(new Entry(painRecords.get(index).date.getTime(), painRecords.get(index).painintensitylevel));

        LineDataSet set = new LineDataSet(entries, "Pain Intensity Level(0-10)");
        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method for show integer
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        set.setValueFormatter(vf);

        set.setLineWidth(2.5f);
        set.setColor(colorleft);
        set.setCircleColor(colorleft);
        set.setCircleRadius(3f);
        set.setFillColor(colorleft);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(colorleft);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);
        LineDataSet setright = new LineDataSet(entriesright, rightlabel);

        setright.setColor(colorright);
        setright.setLineWidth(2.5f);
        setright.setCircleColor(colorright);
        setright.setCircleRadius(3f);
        setright.setFillColor(colorright);
        setright.setMode(LineDataSet.Mode.LINEAR);
        setright.setDrawValues(true);

        setright.setValueTextSize(10f);
        setright.setValueTextColor(colorright);
        setright.setAxisDependency(YAxis.AxisDependency.RIGHT);

        d.addDataSet(setright);
        CombinedData data = new CombinedData();
        data.setData(d);
        chart.setData(data);

        return d;

    }

    public String testCorrelation(List<PainRecord> painRecords,int index) {
        // two column array: 1st column=first array, 1st column=second array
        int n=painRecords.size();
        double [][]data= new double[n][2];
        if(n<3){return null;}
        for (int i=0;i<n;i++)
        {
            data[i][0]=painRecords.get(i).painintensitylevel;
            double t;
            if(index==0)
            {t=painRecords.get(i).temp;}
            else if(index==1)
            {t=painRecords.get(i).humidity;}
            else
                t=painRecords.get(i).pressure;
            data[i][1]=t;
        }
        // create a realmatrix
        RealMatrix m = MatrixUtils.createRealMatrix(data);
        // measure all correlation test: x-x, x-y, y-x, y-x
        for (int i = 0; i < m.getColumnDimension(); i++)
            for (int j = 0; j < m.getColumnDimension(); j++) {
                PearsonsCorrelation pc = new PearsonsCorrelation();
                double cor = pc.correlation(m.getColumn(i), m.getColumn(j));

            }
        // correlation test (another method): x-y
        PearsonsCorrelation pc = new PearsonsCorrelation(m);
        RealMatrix corM = pc.getCorrelationMatrix();
        // significant test of the correlation coefficient (p-value)
        RealMatrix pM = pc.getCorrelationPValues();
        Binding.Pvalue.setText("P value: "+pM.getEntry(0, 1));
        Binding.Rvalue.setText("Correlation R value: "+corM.getEntry(0, 1));
        return ("p value:" + pM.getEntry(0, 1) + "\n" + " correlation: " +
                corM.getEntry(0, 1));
    }
}