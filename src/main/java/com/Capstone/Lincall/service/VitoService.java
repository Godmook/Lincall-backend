package com.Capstone.Lincall.service;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class VitoService {
    String token;

    public VitoService() throws IOException {
        token = getToken();
    }

    public String getToken() throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("vito", Locale.KOREA);
        String clientId = rb.getString("ClientID");
        String clientSecret = rb.getString("ClientSecret");


        URL url = new URL("https://openapi.vito.ai/v1/authenticate");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setDoOutput(true);

        String data = "client_id=" + clientId + "&client_secret=" + clientSecret;

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = httpConn.getOutputStream();
        stream.write(out);

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();
        token = response;

        JSONObject obj = new JSONObject(response);
        return obj.getString("access_token");
    }

    public String requestTranscribe(String filePath) throws Exception {
        URL url = new URL("https://openapi.vito.ai/v1/transcribe");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty("Authorization", "Bearer "+ token);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=authsample");
        httpConn.setDoOutput(true);

        File file = new File(filePath);

        DataOutputStream outputStream;
        outputStream = new DataOutputStream(httpConn.getOutputStream());

        outputStream.writeBytes("--authsample\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() +"\"\r\n");
        outputStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
        outputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
        outputStream.writeBytes("\r\n");

        FileInputStream in =new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            outputStream.write(buffer,0,bytesRead);
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("--authsample\r\n");
        }
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--authsample\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"config\"\r\n");
        outputStream.writeBytes("Content-Type: application/json\r\n");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("{\n  \"diarization\": {\n");
        outputStream.writeBytes("	\"use_verification\": false\n");
        outputStream.writeBytes("	},\n");
        outputStream.writeBytes("\"use_multi_channel\": false,\n");
        outputStream.writeBytes("\"use_itn\": false,\n");
        outputStream.writeBytes("\"use_disfluency_filter\": false,\n");
        outputStream.writeBytes("\"use_profanity_filter\": false,\n");
        outputStream.writeBytes("\"paragraph_splitter\": {\n");
        outputStream.writeBytes("	\"min\": 10,\n");
        outputStream.writeBytes("	\"max\": 50\n");
        outputStream.writeBytes("	}\n");
        outputStream.writeBytes("}");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--authsample\r\n");
        outputStream.flush();
        outputStream.close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();

        JSONObject obj = new JSONObject(response);
        return obj.getString("id");
    }

    public String getTranscribe(String id) throws Exception {
        URL url = new URL("https://openapi.vito.ai/v1/transcribe/"+ id);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty("Authorization", "Bearer " + token);

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();
        return response;
    }
}
