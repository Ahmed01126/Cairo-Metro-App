package com.radwan.metroapp.services;

public class TimeCalculatorImpl implements TimeCalculator {
    @Override
    public String calculateTime(int numberOfStations) {
        int expectedTime = numberOfStations * 2;
        int hours = expectedTime / 60;
        int minutes = expectedTime % 60;

        return hours > 0 ?
                String.format("\n\nExpected time: %d hours and %d minutes", hours, minutes)
                : String.format("\n\nExpected time: %d minutes", minutes);
    }
}
