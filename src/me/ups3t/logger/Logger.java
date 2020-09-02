package me.ups3t.logger;

public class Logger {

    public static void alert(LogType type, String alert) {

        String message = "";

        switch(type) {
            case INFO:
                message += "[INFO] ";
                break;
            case WARNING:
                message += "[WARNING] ";
                break;
            case CRITICAL:
                message += "[CRITICAL] ";
                break;
        }

        message += alert;
        System.out.println(message);

    }

}
