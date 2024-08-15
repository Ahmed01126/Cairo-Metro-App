
package com.radwan.metroapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.radwan.metroapp.services.TextToSpeechService;

import java.util.List;

public class MainActivity extends AppCompatActivity /*implements ShakeDetector.ShakeListener*/ {

    private AutoCompleteTextView startStationAutoComplete;
    private AutoCompleteTextView endStationAutoComplete;
    private TextView summaryText;

    private RouteService routeService;
    private TextToSpeechService textToSpeechService;
    private ShakeDetectionService shakeDetectionService;
    private StationRepository stationRepository;

    private static final String PREVIOUS_DATA_PREF = "previous_data";
    private static final String START_STATION_KEY = "START_STATION";
    private static final String END_STATION_KEY = "END_STATION";

    private SharedPreferences previousData;


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
//        Sensey.getInstance().init(this);
//        Sensey.getInstance().startShakeDetection(this);

        initializeViews();
        initializeServices();
        setupAutoCompleteViews();
        savePreviousData(savedInstanceState);
        setStationAutoComplete();
  }

    private void savePreviousData(Bundle savedInstanceState) {
        String StartStation = startStationAutoComplete.getText().toString();
        String EndStation = endStationAutoComplete.getText().toString();
        previousData = getSharedPreferences(PREVIOUS_DATA_PREF, MODE_PRIVATE);
        if (savedInstanceState != null) {
            StartStation = savedInstanceState.getString(START_STATION_KEY);
            EndStation = savedInstanceState.getString(END_STATION_KEY);
        } else {
            StartStation = previousData.getString(START_STATION_KEY, "");
            EndStation = previousData.getString(END_STATION_KEY, "");
        }
        startStationAutoComplete.setText(StartStation);
        endStationAutoComplete.setText(EndStation);
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

        startStationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String StartStation = (String) parent.getItemAtPosition(position);
            startStationAutoComplete.setText(StartStation);
        });

        // Show all options when the AutoCompleteTextView is clicked or gains focus
        startStationAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStationAutoComplete.showDropDown();
            }
        });


        endStationAutoComplete = findViewById(R.id.endStationAutoComplete);
        endStationAutoComplete.setAdapter(adapter);
        endStationAutoComplete.setThreshold(1);

        endStationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String EndStation = (String) parent.getItemAtPosition(position);
            endStationAutoComplete.setText(EndStation);
        });

        // Show all options when the AutoCompleteTextView is clicked or gains focus
        endStationAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        textToSpeechService = new TextToSpeechService(this);
        shakeDetectionService = new ShakeDetectionService(this);
        shakeDetectionService.startShakeDetection();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(START_STATION_KEY, startStationAutoComplete.getText().toString());
        outState.putString(END_STATION_KEY, endStationAutoComplete.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = previousData.edit();
        editor.putString(START_STATION_KEY, startStationAutoComplete.getText().toString());
        editor.putString(END_STATION_KEY, endStationAutoComplete.getText().toString());
        editor.apply();
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
//        textToSpeechService.speak(routeService.getRouteAnnouncement());
    }

    // Handle the swap button click event
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

//    @Override
//    public void onShakeDetected() {
//        clearFields();
//    }
//
//    @Override
//    public void onShakeStopped() {
//
//    }

    private void clearFields() {
        startStationAutoComplete.setText("");
        endStationAutoComplete.setText("");
        summaryText.setText("");
    }

    /*@Override
    protected void onDestroy() {
        textToSpeechService.shutdown();
        shakeDetectionService.stopShakeDetection();
        super.onDestroy();
    }*/

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}