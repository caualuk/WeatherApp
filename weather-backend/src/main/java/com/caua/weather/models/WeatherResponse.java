package com.caua.weather.models;

import org.json.JSONObject;

public class WeatherResponse {
    private String city;
    private double latitude;
    private double longitude;
    private JSONObject forecast;

    public WeatherResponse(String city, double latitude, double longitude, JSONObject forecast) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.forecast = forecast;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public JSONObject getForecast() {
        return forecast;
    }
}
