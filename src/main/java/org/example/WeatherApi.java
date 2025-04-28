package org.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class WeatherApi {
    private static final String API_KEY = "18fa92b58976d780751b6eda31bdd0a1";
    private static final String BASE_URL = "http://api.weatherstack.com/current";

    public static CompletableFuture<String> fetchWeatherData(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String apiUrl = String.format("%s?access_key=%s&query=%s",
                    BASE_URL, API_KEY, encodedCity);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body) // <-- extract body first
                    .thenApply(WeatherApi::formatJsonResponse);

        } catch (Exception e) {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    private static String formatJsonResponse(String jsonResponse) {
        return jsonResponse.replace(",", ",\n")
                .replace("{", "{\n")
                .replace("}", "\n}");
    }
}
