package me.ups3t.request;

import me.ups3t.ProgramUtils;
import me.ups3t.configuration.Configuration;

import java.io.File;

public class StatusCode {

    private WebRequest request;
    private String statusFull;

    public StatusCode(WebRequest request) {
        this.request = request;
        parseStatus();
    }

    public void parseStatus() {

        File requested = new File(Configuration.getConfig().getWebRoot(), request.getPage());

        if(!requested.exists() || !requested.canRead()) {
            statusFull = "404 Not Found";
        }

        statusFull = "200 OK";

    }

    public String parseStatusFull() {
        return statusFull;
    }

    public String parseStatusText() {
        StringBuilder text = new StringBuilder();
        for(int i = 1; i < statusFull.split(" ").length; i++) {
            text.append(statusFull.split(" ")[i]);
        }
        return text.toString();
    }

    public int parseStatusCode() {
        return Integer.parseInt(statusFull.split(" ")[0]);
    }

}
