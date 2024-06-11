package com.example.iotDataGenerator.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api")
public class PrometheusController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/cpu-usage")
    public String getCpuUsageData(@RequestParam String range) {
        // Construct the URL for Prometheus query
        String url = "http://localhost:9090/api/v1/query_range";
        String query = "system_cpu_usage";

        // Construct the URL with the query




        long end = System.currentTimeMillis() / 1000;
        long start;
        String step = "10s";

        switch (range) {
            case "1m":
                start = end - 60;
                break;
            case "1h":
                start = end - 3600;
                step="10m" ;
                break;
            case "24h":
                start = end - 86400;
                step="2h";
                break;
            default:
                start = end - 60;
                break;
        }


        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", query)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("step", step);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toUri(), String.class);

        return response.getBody();
    }
    @GetMapping("/cpu-usage-statistics")
    public ResponseEntity<?> getCpuUsageDataStatistics(@RequestParam String range) {
        // Construct the URL for Prometheus query
        String prometheusUrl = "http://localhost:9090/api/v1/query";
        String query = "system_cpu_usage[" + range + "]";

        // Construct the URL with the query
        String url = prometheusUrl + "?query=" + query;

        // Make the request to Prometheus
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Parse the response body to find highest and lowest values
        JSONObject responseBody = new JSONObject(response.getBody());
        JSONArray result = responseBody.getJSONObject("data").getJSONArray("result");

        double highestValue = Double.MIN_VALUE;
        double lowestValue = Double.MAX_VALUE;

        for (int i = 0; i < result.length(); i++) {
            JSONObject item = result.getJSONObject(i);
            JSONArray values = item.getJSONArray("values");

            for (int j = 0; j < values.length(); j++) {
                JSONArray valuePair = values.getJSONArray(j);
                double value = valuePair.getDouble(1);

                // Update highest and lowest values
                if (value > highestValue) {
                    highestValue = value;
                }
                if (value < lowestValue) {
                    lowestValue = value;
                }
            }
        }

        // Create a JSON object containing the highest and lowest values
        JSONObject cpuUsageStats = new JSONObject();
        cpuUsageStats.put("highestValue", highestValue);
        cpuUsageStats.put("lowestValue", lowestValue);

        // Return the response with highest and lowest values
        return ResponseEntity.ok().body(cpuUsageStats.toString());
    }
        @GetMapping("/prometheus-query")
    public String queryPrometheus(@RequestParam(name = "range", defaultValue = "1m") String range) {
        String url = "http://localhost:9090/api/v1/query_range";
        String query = "sum(jvm_memory_used_bytes{area=\"heap\"}) by (id)";
        long end = System.currentTimeMillis() / 1000;
        long start;
        String step = "10s";

        switch (range) {
            case "1m":
                start = end - 60;
                break;
            case "1h":
                start = end - 3600;
                step="10m" ;
                break;
            case "24h":
                start = end - 86400;
                step="2h";
                break;
            default:
                start = end - 60;
                break;
        }


        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", query)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("step", step);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toUri(), String.class);

        return response.getBody();
    }


    private static final String PROMETHEUS_URL = "http://localhost:9090/api/v1/query_range";


    @GetMapping("/disk-usage")
    public ResponseEntity<String> getDiskUsage(@RequestParam(name = "range", defaultValue = "1m") String range) {
        String url = "http://localhost:9090/api/v1/query_range";
        String query = "disk_total_bytes - disk_free_bytes";
        long end = System.currentTimeMillis() / 1000;
        long start;

        String step = "10s";

        switch (range) {
            case "1m":
                start = end - 60;
                break;
            case "1h":
                start = end - 3600;
                step="10m" ;
                break;
            case "24h":
                start = end - 86400;
                step="2h";
                break;
            default:
                start = end - 60;
                break;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("query", query)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("step", step);

        ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toUri(), String.class);
        return response;
    }

}

