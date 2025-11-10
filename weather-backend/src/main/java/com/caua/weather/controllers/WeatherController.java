package com.caua.weather.controllers;

import com.caua.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentWeather(@RequestParam String city) {
        JSONObject result = weatherService.getCurrentWeatherByCity(city);
        return ResponseEntity.ok(result.toString());
    }

}
