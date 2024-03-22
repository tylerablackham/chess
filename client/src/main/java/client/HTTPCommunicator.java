package client;
import com.google.gson.Gson;
import model.*;
import model.Error;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPCommunicator {


    public static String sendRequest(String serverUrl, String endpoint, String method, String authToken,
                                     Object requestData) throws IOException {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);
        if (authToken != null && !authToken.isEmpty()) {
            http.setRequestProperty("Authorization", authToken);
        }

        http.connect();

        if (requestData != null) {
            try (OutputStream body = http.getOutputStream()) {
                byte[] input = new Gson().toJson(requestData).getBytes();
                body.write(input, 0, input.length);
            }
        }

        int responseCode = http.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Error error = new Gson().fromJson(response.toString(), Error.class);
                throw new IOException("Server error: " + error.message());
            }
        }
    }
}
