package com.caua.weather.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    private static final String GEOCODE_URL = "https://geocoding-api.open-meteo.com/v1/search?name=";

    public double[] getCoordinates(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GEOCODE_URL + cityName;
        String response = restTemplate.getForObject(url, String.class);

        org.json.JSONObject json = new org.json.JSONObject(response);
        org.json.JSONArray results = json.getJSONArray("results");

        double latitude = results.getJSONObject(0).getDouble("latitude");
        double longitude = results.getJSONObject(0).getDouble("longitude");

        return new double[]{latitude, longitude};
    }
}
