package com.example.paindiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.paindiary.databinding.FragmentHomeBinding;
import com.example.paindiary.weather.GetWeather;
import com.example.paindiary.weather.WeatherResponse;
import com.google.firebase.auth.FirebaseAuth;
public class Home extends Fragment {

    private FragmentHomeBinding Binding;
    private FirebaseAuth mAuth;
    public Home(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment

        mAuth = FirebaseAuth.getInstance();
        Binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        Binding.email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                ToastUtil.newToast(getActivity(),"Logout:success");
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        GetWeather.getCurrentData(new CallBack<WeatherResponse>() {
            @Override
            public void onSucess(WeatherResponse value) {
                //Log.d("tag",Float.toString(value.main.temp));
                Binding.city.setText("City:Suzhou");
                Binding.temp.setText("Temperature:"+value.main.temp+"°C");
                Binding.humidity.setText("Humidity:"+value.main.humidity+"%");
                Binding.pressure.setText("Atmospheric Pressure:"+value.main.pressure+" hpa");
            }

            @Override
            public void onFailure() {

            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Binding = null;
    }
}