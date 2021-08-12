package com.example.paindiary.weather;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paindiary.CallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetWeather {
    public static String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "459087ed348147c22a1b66280bd8e10e";
    public static String city = "Suzhou";
    public static String units="metric";

    public static void getCurrentData(CallBack callBack) {
        //Log.d("abc","def");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(city, AppId,units);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    Log.d("tag","GetWeather success!");
                    callBack.onSucess(weatherResponse);

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.d("tag",t.getMessage());
                callBack.onFailure();
            }
        });
    }
}
