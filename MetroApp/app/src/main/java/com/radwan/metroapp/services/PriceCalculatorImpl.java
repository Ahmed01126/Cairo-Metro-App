package com.radwan.metroapp.services;

public class PriceCalculatorImpl implements PriceCalculator {
    @Override
    public int calculatePrice(int numberOfStations) {
        int price;
        if (numberOfStations <= 16) {
            price = 5;
        } else if (numberOfStations <= 30) {
            price = 7;
        } else {
            price = 10;
        }
        return price;
    }
}
