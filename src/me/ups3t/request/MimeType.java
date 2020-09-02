package me.ups3t.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MimeType {

    private static MimeType instance = null;

    public static MimeType getInstance() {
        if(instance == null) {
            instance = new MimeType();
        }
        return instance;
    }

    public String getMimeTypeFor(String fileExtension) {
        fileExtension = fileExtension.toLowerCase();

        if(fileExtension.endsWith(".htm") || fileExtension.endsWith(".html") || fileExtension.endsWith(".html5")) return "text/html";

        if(fileExtension.endsWith(".png")) return "image/png";

        if(fileExtension.endsWith(".jpeg") || fileExtension.endsWith(".jpg") || fileExtension.endsWith(".jfif") || fileExtension.endsWith(".jpe")) return "image/jpeg";

        if(fileExtension.endsWith(".mp3")) return "audio/mpeg3";

        if(fileExtension.endsWith(".gif")) return "image/gif";

        if(fileExtension.equalsIgnoreCase(".ico")) return "image/vnd.microsoft.icon";

        return "text/plain";
    }

}
