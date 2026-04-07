package com.example.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View; // Добавлен импорт для View

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView result;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        result = findViewById(R.id.result);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location == null) return;

        String provider = location.getProvider();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String message;
        if (LocationManager.GPS_PROVIDER.equals(provider)) {
            message = "GPS: Широта - " + latitude + ", Долгота - " + longitude;
        } else if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            message = "Сеть: Широта - " + latitude + ", Долгота - " + longitude;
        } else {
            message = "Источник: " + provider + "\nШирота: " + latitude + "\nДолгота: " + longitude;
        }

        result.setText(message);
    }

    public void startLocationUpdates(View view) {
        checkPermissionsAndStart();
    }

    private void checkPermissionsAndStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        startListening();
    }

    private void startListening() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

            Toast.makeText(this, "Поиск местоположения запущен...", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка доступа к локации", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            } else {
                Toast.makeText(this, "Разрешение на геопозицию отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
}