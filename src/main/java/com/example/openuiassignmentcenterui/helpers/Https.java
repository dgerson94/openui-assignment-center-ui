package com.example.openuiassignmentcenterui.helpers;

import com.github.kevinsawicki.http.HttpRequest;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Https {

    private static final String PROFESSOR = "professor";

    private static final String HTTP_ERROR = "Http Error";

    private static final String BASIC = "Basic ";
    public static final  String HTTP_ERROR_1 = "This is an Http ";

    public static final String HTTP_ERROR_2 = "error. Your request didn't go through.";
    public static final String AUTH = "Authorization";
    public static final String ERROR = "Error";
    private Https() {
        throw new IllegalStateException("Https class");
    }

    public static StringBuffer httpGet(String userName, String password, String database, String target) {
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL(target);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //THIS IS NEEDED FOR EVERY REQUEST TO SERVER//
            String auth = createAuth(database, userName, password);
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = BASIC + new String(encodedAuth);
            con.setRequestProperty(AUTH, authHeader);
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
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                StringBuilder errorResponse = getErrorStream(con);
                if (errorResponse.toString().contains("server-thrown-error")) {
                    return new StringBuffer("No Submission.");
                } else {
                    Error e = new Error(HTTP_ERROR, HTTP_ERROR_1 + responseCode + HTTP_ERROR_2);
                    e.raiseError();
                    return new StringBuffer(ERROR + responseCode);
                }
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Error e = new Error("Unauthorized", "The FullName or Password is incorrect, please try again");
                e.raiseError();
                return new StringBuffer(ERROR);
            } else {
                Error e = new Error(HTTP_ERROR, HTTP_ERROR_1 + responseCode + HTTP_ERROR_2);
                e.raiseError();
                return new StringBuffer(ERROR + responseCode);
            }
        } catch (IOException e) {
            Error.ioError();
        }
        return response;
    }

    public static void sendJson(String userName, String password, String action, String database, String target, String jsonResponse) throws IOException {
        URL obj = new URL(target);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(action);
        String auth = createAuth(database, userName, password);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = BASIC + new String(encodedAuth);
        con.setRequestProperty(AUTH, authHeader);
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

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_NO_CONTENT) { //success
            System.out.println("GET Response Code :: " + responseCode);
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Error e = new Error("Unauthorized", "The FullName or Password is incorrect, please try again");
            e.raiseError();
        } else {
            Error e = new Error(HTTP_ERROR, HTTP_ERROR_1 + responseCode + HTTP_ERROR_2);
            e.raiseError();
        }
    }


    public static void httpUploadFile(String userName, String password, String database, String target, File file, boolean hasFile) {
        String auth = createAuth(database, userName, password);
        String[] authInfo = auth.split(":");
        HttpRequest request;
        if (hasFile) {
            request = HttpRequest.put(target).basic(authInfo[0], authInfo[1]);
        } else {
            request = HttpRequest.post(target).basic(authInfo[0], authInfo[1]);
        }
        request.part("file", file.getName(), file);
        if (request.ok() || request.created() || request.noContent()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upload Successful");
            alert.setHeaderText("Your file was uploaded successfully.");
            alert.setContentText("Thank you for using our System");
            alert.showAndWait();
        } else {
            Error e = new Error("Upload Failed", "You upload failed with the following response code: " + request.code());
            e.raiseError();
        }
    }


    public static File httpGetFile(String userName, String password, String database, String target, boolean noFileFoundError) throws IOException {
        //there is a default method that noFileFoundError is false. If we want to show the no file found error, then make noFileFoundError true.
        File temp = null;
        URL url = new URL(target);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        String auth = createAuth(database, userName, password);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = BASIC + new String(encodedAuth);
        conn.setRequestProperty(AUTH, authHeaderValue);

        int responseCode = conn.getResponseCode();
        //need to edit this. Deal with different errors
        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return dealWithNotFound(noFileFoundError, conn);
        } else if (responseCode != HttpURLConnection.HTTP_OK) {
            Error e = new Error(ERROR, HTTP_ERROR_1 + responseCode + " error. Your request didn't go through.");
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
        try {
            fileWriter.write(response.toString());
        } finally {
            fileWriter.close();
        }

        in.close();

        conn.disconnect();
        return temp;
    }

    private static File dealWithNotFound(boolean noFileFoundError, HttpURLConnection conn) throws IOException {
        StringBuilder response = getErrorStream(conn);
        if (response.toString().contains("server-thrown-error")) {
            if (noFileFoundError) {
                Error e = new Error("No File Uploaded", "The professor hasn't uploaded a file yet. Check again later.");
                e.raiseError();
            }
            return null;
        } else {
            Error e = new Error("Site Not Found", "We couldn't find the page.");
            e.raiseError();
            return null;
        }
    }

    private static StringBuilder getErrorStream(HttpURLConnection conn) throws IOException {
        InputStream inputStream = conn.getErrorStream();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;
        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);
        in.close();
        return response;
    }

    public static File httpGetFile(String userName, String password, String database, String target) throws IOException {
        return httpGetFile(userName, password, database, target, false);
    }

    private static String createAuth(String database, String userName, String password) {
        String auth;
        if (database.equals(PROFESSOR)) auth = "p" + userName + ":" + password;
        else auth = "s" + userName + ":" + password;
        return auth;
    }
}