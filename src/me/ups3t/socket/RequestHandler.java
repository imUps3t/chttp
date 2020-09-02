package me.ups3t.socket;

import me.ups3t.ProgramUtils;
import me.ups3t.logger.LogType;
import me.ups3t.logger.Logger;
import me.ups3t.request.WebRequest;
import me.ups3t.request.WebResponse;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private Socket client;

    private BufferedReader sock_in;
    private BufferedWriter sock_out;

    public RequestHandler(Socket client) {

        try {

            this.client = client;
            this.sock_in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.sock_out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        }catch (IOException exception) {
            Logger.alert(LogType.CRITICAL, ProgramUtils.PROGRAM_NAME + " has unexpectedly crashed.\nStack trace for nerds:\n" + exception.getMessage());
        }

    }

    @Override
    public void run() {

        while(!Thread.currentThread().isInterrupted()) {
            int i = 0;
            String line;
            String header = "";
            boolean checkHeader = true;
            try {
                 while ((line = sock_in.readLine()) != null) {

                    if(line.isEmpty()) {
                        checkHeader = false;
                        // System.out.println(header);
                        WebRequest request = new WebRequest(header);
                        client.setKeepAlive(request.getConnection() == "keep-alive");
                        WebResponse response = new WebResponse(request);

                        System.out.println("[IP: " + client.getInetAddress().getHostAddress() + "] [Request: " + request.getPage() + "] [Method: " + request.getMethod() + "]");

                        // System.out.println(response.getResponseHeader());
                        // System.out.println(response.getResponseData());

                        sock_out.write(response.getResponseHeader());
                        sock_out.write("\n");
                        sock_out.flush();

                        sock_out.write(response.getResponseData());
                        sock_out.flush();

                        break;
                    }
                    if(checkHeader) {
                        header += line + "\n";
                    }

                 }
                 client.close();
                Thread.currentThread().interrupt();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }


}
