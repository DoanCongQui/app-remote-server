package com.emdeded.controlremoteserver.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Client để giao tiếp với Tinker Board API
 * Base URL: http://192.168.1.15:8000
 */
public class TinkerBoardApiClient {
    private static final String TAG = "TinkerBoardAPI";
    private static final String BASE_URL = "http://100.114.246.3:8000";
    private static final int TIMEOUT = 5000; // 5 seconds

    /**
     * Kiểm tra status của servers và relays
     * GET /api/status
     *
     * @return JSONObject chứa server status và relay states
     * @throws Exception nếu API call thất bại
     */
    public static JSONObject getStatus() throws Exception {
        String url = BASE_URL + "/api/status";
        Log.d(TAG, "Fetching status from: " + url);
        return performGetRequest(url);
    }

    /**
     * Bật/tắt relay
     * POST /api/relay/set
     *
     * @param relayId ID của relay (1, 2, 3)
     * @param state   "on" hoặc "off"
     * @return JSONObject response từ server
     * @throws Exception nếu API call thất bại
     */
    public static JSONObject setRelay(int relayId, String state) throws Exception {
        String url = BASE_URL + "/api/relay/set?relay_id=" + relayId + "&state=" + state;
        Log.d(TAG, "Setting relay " + relayId + " to " + state);
        Log.d(TAG, "URL: " + url);

        return performPostRequest(url, "");
    }

    /**
     * Thực hiện GET request
     *
     * @param urlString URL để gọi
     * @return JSONObject response
     * @throws Exception nếu request thất bại
     */
    private static JSONObject performGetRequest(String urlString) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "GET Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readInputStream(conn.getInputStream());
                Log.d(TAG, "GET Response: " + response);
                return new JSONObject(response);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new Exception("Unauthorized (401)");
            } else {
                String error = readInputStream(conn.getErrorStream());
                throw new Exception("HTTP " + responseCode + ": " + error);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Thực hiện POST request
     *
     * @param urlString URL để gọi
     * @param data      POST data (form-urlencoded)
     * @return JSONObject response
     * @throws Exception nếu request thất bại
     */
    private static JSONObject performPostRequest(String urlString, String data) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Gửi data
            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                wr.write(data);
                wr.flush();
            }

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "POST Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readInputStream(conn.getInputStream());
                Log.d(TAG, "POST Response: " + response);
                return new JSONObject(response);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new Exception("Unauthorized (401)");
            } else {
                String error = readInputStream(conn.getErrorStream());
                throw new Exception("HTTP " + responseCode + ": " + error);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Đọc InputStream và chuyển thành String
     *
     * @param is InputStream để đọc
     * @return String content
     * @throws IOException nếu có lỗi đọc
     */
    private static String readInputStream(InputStream is) throws IOException {
        if (is == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * Parse relay states từ API response
     *
     * @param response JSONObject từ /api/status
     * @return List<Map> chứa relay information
     */
    public static List<Map<String, Object>> parseRelayStates(JSONObject response) {
        List<Map<String, Object>> relays = new ArrayList<>();
        try {
            JSONArray relayArray = response.getJSONArray("relays");
            for (int i = 0; i < relayArray.length(); i++) {
                JSONObject relay = relayArray.getJSONObject(i);
                Map<String, Object> relayMap = new HashMap<>();
                relayMap.put("id", relay.getInt("id"));
                relayMap.put("state", relay.getBoolean("state"));
                relays.add(relayMap);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing relay states", e);
        }
        return relays;
    }

    /**
     * Parse server status từ API response
     *
     * @param response JSONObject từ /api/status
     * @return List<Map> chứa server information
     */
    public static List<Map<String, Object>> parseServerStatus(JSONObject response) {
        List<Map<String, Object>> servers = new ArrayList<>();
        try {
            JSONArray serverArray = response.getJSONArray("servers");
            for (int i = 0; i < serverArray.length(); i++) {
                JSONObject server = serverArray.getJSONObject(i);
                Map<String, Object> serverMap = new HashMap<>();
                serverMap.put("name", server.getString("name"));
                serverMap.put("ip", server.getString("ip"));
                serverMap.put("online", server.getBoolean("online"));
                serverMap.put("ssh", server.getBoolean("ssh"));
                servers.add(serverMap);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing server status", e);
        }
        return servers;
    }
}
