package com.radwan.metroapp.services;

public interface RouteFormatter {
    void getInitialDirection(String[] stations);
    String getRouteString(String[] stations);

    String getInitialDirection();
    StringBuilder getRoute();
}
