package me.ups3t.request;

import me.ups3t.logger.LogType;
import me.ups3t.logger.Logger;

import java.util.HashMap;

public class WebRequest {

    private String request;
    private HashMap<String, String> headers;
    private String requestMeta;

    public WebRequest(String request) {

        this.request = request;
        this.headers = new HashMap<>();

        requestMeta = request.split("\n")[0];
        if(requestMeta.split(" ").length != 3) {
            Logger.alert(LogType.WARNING, "Invalid web request! (Too many arguments)");
        }
        for(int i = 1; i < request.split("\n").length; i++) {

            headers.put(request.split("\n")[i].split(": ")[0], request.split("\n")[i].split(": ")[1]);
        }

    }


    public String getMethod() {
        return requestMeta.split(" ")[0].toUpperCase();
    }

    public String getPage() {
        return requestMeta.split(" ")[1];
    }

    public String getHttpVersion() {
        return requestMeta.split(" ")[2];
    }

    public String getHeader(String header) {
        String result = "null";
        if(headers.containsKey(header)) result = headers.get(header);

        return result;
    }

    public String getHost() {
        return getHeader("Host");
    }

    public String getReferer() {
        return getHeader("Referer");
    }

    public String getUserAgent() {
        return getHeader("User-Agent");
    }

    public String getAcceptCharset() {
        return getHeader("Accept-Charset");
    }

    public String getAcceptLanguage() {
        return getHeader("Accept-Language");
    }

    public String getAcceptEncoding() {
        return getHeader("Accept-Encoding");
    }

    public String getIfModifiedSince() {
        return getHeader("If-Modified-Since");
    }

    public String getIfUnmodifiedSince() {
        return getHeader("If-Unmodified-Since");
    }

    public String getConnection() {return getHeader("Connection");}

}
