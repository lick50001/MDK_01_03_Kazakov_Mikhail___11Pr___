package com.example.gps;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    LocationManager _LocationManager;
    int ACCESS_FINE_LOCATION;
    int ACCESS_COARSE_LOCATION;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        };
    }

    @Override
    public void onLocationChanged(@NonNull Location location){
    if (location == null) return;
    else {
        String message = "";
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)){
            message += "\nМестоположение определено с помощью GPS: долгота -" + location.getLatitude() + "широта -" + location.getLongitude();
        }
        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)){
            message += "\nМестоположение определено с помощью интернета: долгота -" + location.getLatitude() + "широта -" + location.getLongitude();
        }
        result.setText(message);
    }
}