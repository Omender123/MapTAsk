package com.example.mymaptask;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mymaptask.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.Stack;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<LatLng> locationArrayList;
    TextView text, text1;
    boolean click = false;
    ArrayList<Boolean> focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationArrayList = new ArrayList<>();
        locationArrayList.clear();
        locationArrayList.add(new LatLng(30.7333, 76.7794));
        locationArrayList.add(new LatLng(28.7041, 77.1025));
        locationArrayList.add(new LatLng(31.3260, 75.5762));
        locationArrayList.add(new LatLng(30.3752, 76.7821));
        locationArrayList.add(new LatLng(30.7333, 76.7794));
        locationArrayList.add(new LatLng(30.9578, 76.7914));

        binding.floating.setOnClickListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        focus = new ArrayList<Boolean>();
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        for (int i = 0; i < locationArrayList.size(); i++) {
            focus.add(false);

            mMap.addMarker(new MarkerOptions()
                    .position(locationArrayList.get(i))
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker))));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
            float zoomLevel = 8.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    locationArrayList.get(0), zoomLevel));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }

    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.image);
        text = (TextView) customMarkerView.findViewById(R.id.text);
        text1 = (TextView) customMarkerView.findViewById(R.id.text1);


        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.floating:

                if (click) {
                    click = false;
                    text.setVisibility(View.GONE);
                    text1.setVisibility(View.GONE);
                    binding.floating.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_location_searching_24));
                } else {
                    click = true;
                    text.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);

                    binding.floating.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_my_location_24));

                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        LatLng position = marker.getPosition();
        int pos = 0;

        for (int i = 0; i < locationArrayList.size(); i++) {
            if (position.equals(locationArrayList.get(i))) {
                pos = i;
                if (focus.get(pos)) {
                    focus.set(pos, false);
                    float zoomLevel = 8.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            locationArrayList.get(pos), zoomLevel));
                } else {
                    focus.set(pos, true);
                    float zoomLevel = 17.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            locationArrayList.get(pos), zoomLevel));
                }
            }
        }

        return true;
    }
}