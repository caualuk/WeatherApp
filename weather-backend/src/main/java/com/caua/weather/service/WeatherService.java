package com.caua.weather.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    private final GeocodingService geocodingService;
    private static final String WEATHER_URL = "https://api.open-meteo.com/v1/forecast";

    public WeatherService(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    public JSONObject getCurrentWeatherByCity(String cityName) {
        try {
            // 1️⃣ Buscar coordenadas da cidade
            double[] coordinates = geocodingService.getCoordinates(cityName);
            double latitude = coordinates[0];
            double longitude = coordinates[1];

            // 2️⃣ Buscar dados da previsão (hora a hora)
            String url = WEATHER_URL +
                    "?latitude=" + latitude +
                    "&longitude=" + longitude +
                    "&hourly=temperature_2m,relative_humidity_2m,precipitation" +
                    "&timezone=auto";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // 3️⃣ Tratar dados do JSON
            JSONObject json = new JSONObject(response);
            JSONObject hourly = json.getJSONObject("hourly");

            JSONArray times = hourly.getJSONArray("time");
            JSONArray temps = hourly.getJSONArray("temperature_2m");
            JSONArray humidity = hourly.getJSONArray("relative_humidity_2m");
            JSONArray rain = hourly.getJSONArray("precipitation");

            // 4️⃣ Descobrir o horário mais próximo de agora
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            LocalDateTime now = LocalDateTime.now();
            int closestIndex = 0;
            long minDifference = Long.MAX_VALUE;

            for (int i = 0; i < times.length(); i++) {
                LocalDateTime forecastTime = LocalDateTime.parse(times.getString(i), inputFormatter);
                long diff = Math.abs(Duration.between(now, forecastTime).toMinutes());
                if (diff < minDifference) {
                    minDifference = diff;
                    closestIndex = i;
                }
            }

            // 5️⃣ Formatando o horário
            LocalDateTime forecastTime = LocalDateTime.parse(times.getString(closestIndex), inputFormatter);
            String formattedTime = forecastTime.format(outputFormatter) + " (horário aproximado)";

            // 6️⃣ Criar o resultado
            JSONObject result = new JSONObject();
            result.put("city", cityName);
            result.put("latitude", latitude);
            result.put("longitude", longitude);
            result.put("time", formattedTime);
            result.put("temperature", temps.getDouble(closestIndex));
            result.put("humidity", humidity.getDouble(closestIndex));
            result.put("precipitation", rain.getDouble(closestIndex));

            return result;

        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", "Erro ao buscar clima: " + e.getMessage());
            return error;
        }
    }
}
