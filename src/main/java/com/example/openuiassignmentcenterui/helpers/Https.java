package com.example.openuiassignmentcenterui.helpers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.github.kevinsawicki.http.HttpRequest;
public class Https {

    public static final String PROFESSOR = "professor";
    private static final String USER_AGENT = "Mozilla/5.0";

    public static StringBuffer httpGet(String user_name, String password, String database, String target) throws IOException {
        StringBuffer response = null;
        if (database == PROFESSOR) {
            URL url = new URL(target);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //THIS IS NEEDED FOR EVERY REQUEST TO SERVER//
            String auth = "p" + user_name + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            con.setRequestProperty("Authorization", authHeader);
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success, need to add error for failure
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Error e = new Error("Unauthorized", "The FullName or Password is incorrect, please try again");
                e.raiseError();
                return null;
            }
        }
        return response;
    }

    public static StringBuffer sendJson(String user_name, String password, String action, String database, String target, String jsonResponse) throws IOException {
        //TODO: Need to fix this up.
        StringBuffer response = new StringBuffer();
        URL obj = new URL(target);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(action);
        String auth = "p" + user_name + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        con.setRequestProperty("Authorization", authHeader);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        // For POST only - START
        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        String jsonString = jsonResponse;
        os.write(jsonString);
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println(action + " Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new IllegalArgumentException("Unauthorized: The username or password is incorrect");
        }
        con.disconnect();
        return response;
    }


    public static void httpPutFile(String user_name, String password, String database, String target, File file) {
        HttpRequest request = HttpRequest.post(target).basic("p"+ user_name , password);
        request.part("file", file.getName(),file);
        if (request.ok())
            System.out.println("Status was updated");
    }


    public static File httpGetFile(String user_name, String password, String database, String target) throws IOException {
        File temp = null;
        URL url = new URL(target);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        String auth = "p" + user_name + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);
        conn.setRequestProperty("Authorization", authHeaderValue);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        String contentDisposition = conn.getHeaderField("Content-Disposition");
        String fileNameHeader = null;
        if (contentDisposition != null && contentDisposition.contains("filename=")) {
            fileNameHeader = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9).replace("\"", "");
            temp = new File(fileNameHeader);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            System.out.println(inputLine);
        }
        System.out.println(response);
        FileWriter fileWriter = new FileWriter(temp);
        fileWriter.write(response.toString());

        // Close the FileWriter
        fileWriter.close();
        in.close();

        conn.disconnect();
        return temp;
    }
}