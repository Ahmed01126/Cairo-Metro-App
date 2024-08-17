
package com.radwan.metroapp;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.radwan.metroapp.R;
import com.radwan.metroapp.repositories.StationRepository;
import com.radwan.metroapp.services.RouteService;
import com.radwan.metroapp.services.ShakeDetectionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements AirLocation.Callback {
    private AutoCompleteTextView startStationAutoComplete;
    private AutoCompleteTextView endStationAutoComplete;
    private TextView summaryText;
    private RouteService routeService;
    private String address;
    private StationRepository stationRepository;

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
        Sensey.getInstance().init(this);
        AirLocation airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();
        initializeViews();
        initializeServices();
        setupAutoCompleteViews();
        setStationAutoComplete();
    }

    private void setStationAutoComplete() {
        if (startStationAutoComplete.isFocused()) {
            startStationAutoComplete.setText(startStationAutoComplete.getText().toString());
        } else if (endStationAutoComplete.isFocused()) {
            endStationAutoComplete.setText(endStationAutoComplete.getText().toString());
        }
    }

    private void setupAutoCompleteViews() {
        List<String> totalStations = stationRepository.getTotalStations();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,totalStations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startStationAutoComplete = findViewById(R.id.startStationAutoComplete);
        startStationAutoComplete.setAdapter(adapter);
        startStationAutoComplete.setThreshold(1);

        endStationAutoComplete = findViewById(R.id.endStationAutoComplete);
        endStationAutoComplete.setAdapter(adapter);
        endStationAutoComplete.setThreshold(1);

        startStationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String StartStation = (String) parent.getItemAtPosition(position);
            startStationAutoComplete.setText(StartStation);
        });

        endStationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String EndStation = (String) parent.getItemAtPosition(position);
            endStationAutoComplete.setText(EndStation);
        });

        // Show all options when the AutoCompleteTextView is clicked or gains focus
        startStationAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStationAutoComplete.setText("");
                startStationAutoComplete.showDropDown();
            }
        });

        // Show all options when the AutoCompleteTextView is clicked or gains focus
        endStationAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endStationAutoComplete.setText("");
                endStationAutoComplete.showDropDown();
            }
        });
    }

    private void initializeViews() {
        startStationAutoComplete = findViewById(R.id.startStationAutoComplete);
        endStationAutoComplete = findViewById(R.id.endStationAutoComplete);
        summaryText = findViewById(R.id.summaryText);
    }

    private void initializeServices() {
        stationRepository = new StationRepository();
        routeService = new RouteService(stationRepository);
        ShakeDetectionService shakeDetectionService = new ShakeDetectionService(this);
        shakeDetectionService.startShakeDetection();
    }

    // Handle the done button click
    public void onDoneButtonClick(View view) {
        String startStation = startStationAutoComplete.getText().toString();
        String endStation = endStationAutoComplete.getText().toString();

        if (!validateStations(startStation, endStation)) {
            return;
        }

        String routeSummary = routeService.getRouteSummary(startStation, endStation);
        summaryText.setText(routeSummary);
    }

    // Handle the map icon click event
    public void goMapOnClick(View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        startActivity(mapIntent);
    }

    // Handle the swap icon click event
    public void swap_station(View view) {
        String temp = startStationAutoComplete.getText().toString();
        startStationAutoComplete.setText(endStationAutoComplete.getText().toString());
        endStationAutoComplete.setText(temp);
        onDoneButtonClick(view);
    }

    private boolean validateStations(String StartStation, String EndStation) {
        if(StartStation.equals(EndStation)) {
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            return false;
        }
        return validateSpinners(startStationAutoComplete, StartStation)
            && validateSpinners(endStationAutoComplete, EndStation);
    }

    private boolean validateSpinners(AutoCompleteTextView stationAutoComplete, String station) {
        List<String> totalStations = stationRepository.getTotalStations();
        totalStations = stationRepository.getTotalStations();
        if(station.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).playOn(stationAutoComplete);
            Toast.makeText(this, "Please select a station", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!totalStations.contains(station)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(stationAutoComplete);
            Toast.makeText(this, "Station not found, Please select a valid station", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        startStationAutoComplete.setText("");
        endStationAutoComplete.setText("");
        summaryText.setText("");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "Error in getting location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> locations) {
        double latitude = locations.get(0).getLatitude();
        double longitude = locations.get(0).getLongitude();
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            assert addressList != null;
            address = addressList.get(0).getAddressLine(0);
            startStationAutoComplete.setText(address);
        } catch (IOException e) {
            Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
        }
    }

}