package me.ups3t.socket;

import me.ups3t.ProgramUtils;
import me.ups3t.configuration.Configuration;
import me.ups3t.logger.LogType;
import me.ups3t.logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Listener {

    private static Listener instance = null;

    public static Listener getInstance() {
        if(instance == null) {
            instance = new Listener();
        }
        return instance;
    }

    private boolean listening = false;

    private final List<Thread> threads = new ArrayList<>();

    public void startListening(int port) {

        ServerSocket socket = null;

        try {
            socket = new ServerSocket(port);
        }catch (IOException exception) {
            Logger.alert(LogType.CRITICAL, ProgramUtils.PROGRAM_NAME + " has unexpectedly crashed.\nStack trace for nerds:\n" + exception.getMessage());
        }

        listening = true;
        ServerSocket finalSocket = socket;
        Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while(listening) {
                        try {
                            Socket client = finalSocket.accept();
                            Thread deliverer = new Thread(new RequestHandler(client));
                            deliverer.start();
                            threads.add(deliverer);
                            Logger.alert(LogType.INFO, "Connection received");
                        } catch (IOException exception) {
                            Logger.alert(LogType.CRITICAL, ProgramUtils.PROGRAM_NAME + " has unexpectedly crashed.\nStack trace for nerds:\n" + exception.getMessage());
                        }
                    }
                }
            });

        thread.start();

        Logger.alert(LogType.INFO, "Began listening for clients on port " + port);

    }

    public void stopListening() {
        for(int i = 0; i < threads.size(); i++) {
            threads.get(i).interrupt();
            Logger.alert(LogType.INFO, "Closed Thread " + threads.get(i).getName());
        }

        Logger.alert(LogType.INFO, "Successfully closed sockets.");
    }

}
