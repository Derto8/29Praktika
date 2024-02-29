package com.example.a29;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;


public class MainActivity extends AppCompatActivity {

    String API_KEY = "ea2b82e2-e755-4f65-ad39-c512f6db0cf8";
    MapView mapView;
    Map map;
    UserLocationLayer locationLayer;
    private LocationManager locationManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey(API_KEY);
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.map);
        map = mapView.getMap();

        locationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        locationManager = MapKitFactory.getInstance().createLocationManager();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            setDataLocation();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    133
            );
        }
    }

    void setDataLocation() {

        map.move(
                new CameraPosition(
                        new Point(58.01123789949114,56.25446366030623), 15f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null
        );
        map.getMapObjects().addPlacemark(
                new Point(55.751574, 38.573856),
                ImageProvider.fromResource(MainActivity.this, R.mipmap.ic_launcher));


        locationManager.requestSingleUpdate(new LocationListener() {

            @Override
            public void onLocationUpdated(@NonNull com.yandex.mapkit.location.Location location) {
                double latitude = location.getPosition().getLatitude();
                double longitude = location.getPosition().getLongitude();

                CameraPosition cameraPosition = new CameraPosition(
                        new Point(latitude, longitude),
                        20.0f, 0.0f, 0.0f
                );
                map.move(cameraPosition, new Animation(Animation.Type.SMOOTH, 5), null);
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
}