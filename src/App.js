import React, { useState } from "react";
import "./App.css";

// Importações do Font Awesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTemperatureHigh,
  faDroplet,
  faCloudRain,
  faLocationDot,
  faClock,
  faMagnifyingGlass,
} from "@fortawesome/free-solid-svg-icons";

function App() {
  const [city, setCity] = useState("");
  const [weather, setWeather] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchWeather = async () => {
    if (!city) return;
    setLoading(true);
    setError("");
    try {
      const response = await fetch(
        `http://localhost:8080/weather/current?city=${city}`
      );
      const data = await response.json();
      if (data.error) throw new Error(data.error);
      setWeather(data);
    } catch (err) {
      setError("Cidade não encontrada ou erro no servidor");
      setWeather(null);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      fetchWeather();
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>
          <FontAwesomeIcon icon={faTemperatureHigh} /> Weather App
        </h1>

        <div className="search-bar">
          <input
            type="text"
            placeholder="Digite o nome da cidade..."
            value={city}
            onChange={(e) => setCity(e.target.value)}
            onKeyDown={handleKeyPress}
          />
          <button onClick={fetchWeather}>
            <FontAwesomeIcon icon={faMagnifyingGlass} />
          </button>
        </div>

        {loading && <p>Carregando...</p>}
        {error && <p className="error">{error}</p>}

        {weather && (
          <div className="weather-info">
            <h2>
              <FontAwesomeIcon icon={faLocationDot} /> {weather.city}
            </h2>
            <p>
              <FontAwesomeIcon icon={faTemperatureHigh} /> Temperatura:{" "}
              {weather.temperature}°C
            </p>
            <p>
              <FontAwesomeIcon icon={faDroplet} /> Umidade: {weather.humidity}%
            </p>
            <p>
              <FontAwesomeIcon icon={faCloudRain} /> Precipitação:{" "}
              {weather.precipitation} mm
            </p>
            <p>
              <FontAwesomeIcon icon={faClock} /> Atualizado: {weather.time}
            </p>
            <p className="coords-lat">
              Latitude: {weather.latitude}
            </p>
            <p className="coords-lon">
              Longitude: {weather.longitude}
            </p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
