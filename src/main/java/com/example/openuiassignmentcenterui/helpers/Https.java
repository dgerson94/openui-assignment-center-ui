package com.example.openuiassignmentcenterui.helpers;

import com.github.kevinsawicki.http.HttpRequest;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Https {

    private static final String PROFESSOR = "professor";

    public static StringBuffer httpGet(String user_name, String password, String database, String target)  {
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL(target);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //THIS IS NEEDED FOR EVERY REQUEST TO SERVER//
            String auth = createAuth(database, user_name, password);
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
                return new StringBuffer("Error");
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                Error e = new Error("Not Found", "We couldn't find what you asked for, please verify your info.");
                e.raiseError();
                return new StringBuffer("Error 404");
            } else {
                Error e = new Error("Http Error", "This is an Http " + responseCode + "error. Your request didn't go through.");
                e.raiseError();
                return new StringBuffer("Error" + responseCode);
            }
        } catch (IOException e) {
            Error.ioError();
        }
        return response;
    }

    public static StringBuffer sendJson(String user_name, String password, String action, String database, String target, String jsonResponse) throws IOException {
        StringBuffer response = new StringBuffer();
        URL obj = new URL(target);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(action);
        String auth = createAuth(database, user_name, password);
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
            return null;
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Error e = new Error("Unauthorized", "The FullName or Password is incorrect, please try again");
            e.raiseError();
            return null;
        } else {
            Error e = new Error("Http Error", "This is an Http " + responseCode + "error. Your request didn't go through.");
            e.raiseError();
            return null;
        }
    }


    public static void httpPutFile(String user_name, String password, String database, String target, File file) {
        String auth = createAuth(database, user_name, password);
        String[] authInfo = auth.split(":");
        HttpRequest request = HttpRequest.put(target).basic(authInfo[0], authInfo[1]);
        request.part("file", file.getName(), file);
        if (request.ok() || request.created()){
            //TODO: Make new helper class that does alerts.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upload Successful");
            alert.setHeaderText("Your file was uploaded successfully.");
            alert.setContentText("Thank you for using our System");
            alert.showAndWait();
        }
        else{
            Error e = new Error("Upload Failed","You upload failed with the following response code: " + request.code());
            e.raiseError();
        }
    }


    public static File httpGetFile(String user_name, String password, String database, String target) throws IOException {
        File temp = null;
        URL url = new URL(target);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        String auth = createAuth(database, user_name, password);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);
        conn.setRequestProperty("Authorization", authHeaderValue);

        int responseCode = conn.getResponseCode();
        //need to edit this. Deal with different errors
        if (responseCode != HttpURLConnection.HTTP_OK) {
            Error e = new Error("Error", "This is an Http " + responseCode + "error. Your request didn't go through.");
            e.raiseError();
            return null;
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

    private static String createAuth(String database, String user_name, String password) {
        String auth;
        if (database == PROFESSOR) auth = "p" + user_name + ":" + password;
        else auth = "s" + user_name + ":" + password;
        return auth;
    }
}