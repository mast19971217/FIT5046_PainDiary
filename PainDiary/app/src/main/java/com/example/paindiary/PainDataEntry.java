package com.example.paindiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paindiary.databinding.FragmentPainDataEntryBinding;
import com.example.paindiary.room.PainRecord;
import com.example.paindiary.room.PainRecordViewModel;
import com.example.paindiary.weather.GetWeather;
import com.example.paindiary.weather.WeatherResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


public class PainDataEntry extends Fragment {
    private FragmentPainDataEntryBinding Binding;
    private FirebaseAuth mAuth;
    private PainRecordViewModel painRecordViewModel;
    private Button button;
    private TimePicker timePicker;
    private String email;
    AlarmManager manager;
    public PainDataEntry(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();
        Binding = FragmentPainDataEntryBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        button=Binding.button1;
        timePicker=Binding.pick;
        mAuth = FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail();
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //闹钟启动时才会跳转
                Intent intent=new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);
                 manager= (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
                Toast.makeText(getActivity(), "Set Success!", Toast.LENGTH_LONG).show();

            }
        });
        Binding.seekBarpainlevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Binding.textViewpainlevel.setText(progress+"");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        painRecordViewModel  = new
                ViewModelProvider(this).get(PainRecordViewModel.class);
        painRecordViewModel.initalizeVars(getActivity().getApplication());

        Date curtime = new Date(System.currentTimeMillis());
        painRecordViewModel.findByDate
                (curtime, email,new CallBack<PainRecord>()
        {
                    @Override
                    public void onSucess(PainRecord painRecord)
                    {
                        getActivity().runOnUiThread(() -> {
                            if (painRecord != null) {
                                Off();
                                Binding.editTextNumber2.setText(painRecord.steps + "");
                                Binding.editTextNumber.setText(painRecord.stepsaim + "");
                                Binding.seekBarpainlevel.setProgress(painRecord.painintensitylevel);
                                SpinnerAdapter apsAdapter = Binding.spinnerlocation.getAdapter(); //得到SpinnerAdapter对象
                                int k = apsAdapter.getCount();
                                for (int i = 0; i < k; i++)
                                    if (painRecord.painlocation.equals(apsAdapter.getItem(i).toString())) {
                                        Binding.spinnerlocation.setSelection(i, true);// 默认选中项
                                        break;
                                    }
                                ((RadioButton) Binding.groupDivider.getChildAt(painRecord.moodlevel)).setChecked(true);
                                ToastUtil.newToast(getActivity(), "Today's Pain!");
                            }
                            else
                                {

                                            if (painRecord == null) {
                                                On();
                                                Binding.editTextNumber2.setText("0");
                                                Binding.editTextNumber.setText("10000");
                                                Binding.seekBarpainlevel.setProgress(0);
                                                Binding.spinnerlocation.setSelection(0, true);// 默认选中项
                                                ((RadioButton) Binding.groupDivider.getChildAt(0)).setChecked(true);
                                                ToastUtil.newToast(getActivity(), "Please Record today's Pain!");
                                            }


                                    };
                        });

                        }

            @Override
            public void onFailure() {
                Looper.prepare();
                ToastUtil.newToast(getActivity(),"fail to load data!");
                Looper.loop();
            }

        }


                );
        Binding.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                On();
                ToastUtil.newToast(getActivity(),"Please Edit!");
            }
        });


                Binding.save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Binding.editTextNumber.getText() == null || Binding.editTextNumber.getText().toString().isEmpty()
                                || Binding.editTextNumber2.getText() == null || Binding.editTextNumber2.getText().toString().isEmpty()) {
                            ToastUtil.newToast(getActivity(), "Please Input Your Steps!");
                            return;
                        }
                        String location = Binding.spinnerlocation.getSelectedItem().toString();
                        String email = mAuth.getCurrentUser().getEmail();
                        int moodlevel = Binding.groupDivider.indexOfChild(Binding.groupDivider.findViewById(Binding.groupDivider.getCheckedRadioButtonId()));
                        int painlevel = Binding.seekBarpainlevel.getProgress();
                        int stepsaim = Integer.parseInt(Binding.editTextNumber.getText().toString());
                        int steps = Integer.parseInt(Binding.editTextNumber2.getText().toString());
                        try {
                            GetWeather.getCurrentData(new CallBack<WeatherResponse>() {
                                @Override
                                public void onSucess(WeatherResponse value) {
                                    float temp = value.main.temp;
                                    float humidity = value.main.humidity;
                                    float pressure = value.main.pressure;
                                    Date curtime = new Date(System.currentTimeMillis());
                                    PainRecord iou = new PainRecord(email, painlevel, location, moodlevel, stepsaim, steps, temp, humidity, pressure);
                                    painRecordViewModel.findByDate(curtime, email,new CallBack<PainRecord>() {
                                        @Override
                                        public void onSucess(PainRecord painRecord) {
                                            if (painRecord == null)
                                                painRecordViewModel.insert(iou);
                                            else {
                                                painRecordViewModel.updateBydate(curtime, email, painlevel, location, moodlevel, stepsaim, steps, temp, humidity, pressure);

                                            }
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Off();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure() {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {

                                }
                            });

                        } catch (Exception e) {
                            ToastUtil.newToast(getActivity(), "Save Failure!");
                        }
                        ToastUtil.newToast(getActivity(), "Save Success!");


                    }
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Binding = null;
    }

    public void enableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }
    }

    public void disableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }

    }

    public void Off()
    {
        disableRadioGroup(Binding.groupDivider);
        Binding.spinnerlocation.setEnabled(false);
        Binding.save.setEnabled(false);
        Binding.Edit.setEnabled(true);
        Binding.seekBarpainlevel.setEnabled(false);
        Binding.editTextNumber.setEnabled(false);
        Binding.editTextNumber2.setEnabled(false);
    }
    public void On()
    {
        enableRadioGroup(Binding.groupDivider);
        Binding.spinnerlocation.setEnabled(true);
        Binding.save.setEnabled(true);
        Binding.Edit.setEnabled(false);
        Binding.seekBarpainlevel.setEnabled(true);
        Binding.editTextNumber.setEnabled(true);
        Binding.editTextNumber2.setEnabled(true);
    }
}