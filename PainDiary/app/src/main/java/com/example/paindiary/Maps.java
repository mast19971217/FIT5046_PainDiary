package com.example.paindiary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.paindiary.databinding.FragmentMapsBinding;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;
import com.microsoft.maps.search.MapLocationOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Maps extends Fragment {
    private FragmentMapsBinding Binding;
    private MapView mMapView;
    private MapImage mPinImage;
    private MapElementLayer mPinLayer;
    private int mUntitledPushpinCount = 0;
    private static final Geopoint  Suzhou=new Geopoint(31.29, 120.59);
    public Maps(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        super.onCreate(savedInstanceState);
        mMapView = new MapView(getActivity(), MapRenderMode.RASTER);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        FrameLayout t=Binding.mapView;
        t.addView(mMapView);
        mMapView.onCreate(savedInstanceState);
        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();

        MapLocationOptions options = new MapLocationOptions().setMaxResults(1);



        Binding.Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPinLayer.getElements().clear();
                MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken("pk.eyJ1IjoibWFzdDEyMTciLCJhIjoiY2tvbnE5YXpzMDM5ZjJvcXNiMWUzczZudiJ9.SiyjYgFtD_-zrpKHHn_QrA")
                        .proximity(Point.fromLngLat(120.59,31.29))
                        .query(Binding.Address.getText().toString())
                        .build();
                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                        List<CarmenFeature> results = response.body().features();

                        if (results.size() > 0) {

                            // Log the first results Point.
                            Point firstResultPoint = results.get(0).center();
                            Log.d("tag",results.get(0).placeName());
                            Geopoint p=new Geopoint(firstResultPoint.latitude(),firstResultPoint.longitude());
                            addPin(p);
                            mMapView.setScene(MapScene.createFromLocation(p), MapAnimationKind.DEFAULT);

                        } else {
                            Looper.prepare();
                            ToastUtil.newToast(getActivity(),"no result!");
                            Looper.loop();

                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
        });

        return view;



    }
    private MapImage getPinImage() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin, null);

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return new MapImage(bitmap);
    }
    private void addPin(Geopoint location) {
        MapIcon pushpin = new MapIcon();
        pushpin.setLocation(location);

        pushpin.setImage(mPinImage);
        pushpin.setNormalizedAnchorPoint(new PointF(0.5f, 1f));

        mPinLayer.getElements().add(pushpin);
    }


    @Override
    public void onStart() {
        super.onStart();
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(Suzhou, 13),
                MapAnimationKind.NONE);
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
        Binding = null;
    }

}