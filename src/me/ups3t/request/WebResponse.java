package me.ups3t.request;

import me.ups3t.ProgramUtils;
import me.ups3t.configuration.Configuration;
import me.ups3t.logger.LogType;
import me.ups3t.logger.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WebResponse {

    private HashMap<String, String> headers;
    private WebRequest request;
    private String responseHeader = "";
    private String responseData = "";
    public int fileLength;

    public WebResponse(WebRequest request) {
        this.request = request;
        headers = new HashMap<>();
        respond();
    }

    private void respond() {
        File requestedFile = null;
        if (request.getPage().equalsIgnoreCase("/")) {
            requestedFile = new File(Configuration.getConfig().getWebRoot(), Configuration.getConfig().getDefaultPage());
        } else {
            requestedFile = new File(Configuration.getConfig().getWebRoot(), request.getPage());
        }

        // Returning Response Headers

        StatusCode statusCode = new StatusCode(request);

        // Add option to change 404 site in settings
        if(statusCode.parseStatusCode() == 404) requestedFile = new File(Configuration.getConfig().getWebRoot(), Configuration.getConfig().getNotFoundPage());

        fileLength = (int) requestedFile.length();

        responseHeader = request.getHttpVersion() + " " + statusCode.parseStatusFull() + "\n";
        headers.put("Content-Type", MimeType.getInstance().getMimeTypeFor(requestedFile.getName()) + "; charset=UTF-8");
        headers.put("Content-Length", String.valueOf(fileLength));

        for(Map.Entry<String, String> entry : headers.entrySet()){
            responseHeader += entry.getKey() + ": " + entry.getValue() + "\n";
        }

        // Returning HTML code

        BufferedReader file_in = null;

        try {
            file_in = new BufferedReader(new FileReader(requestedFile));
        } catch (FileNotFoundException ex) {
            Logger.alert(LogType.CRITICAL, "Could not open requested file! Stack trace for nerds:\n" + ex.getMessage());
        }

        StringBuilder code = new StringBuilder();
        String line = "";

        try {
            while ((line = file_in.readLine()) != null)
                    code.append(line);
        } catch (IOException ex) {

        }

        responseData = code.toString();


    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public String getResponseData() {
        return responseData;
    }

}
