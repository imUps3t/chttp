package me.ups3t;

import me.ups3t.configuration.Configuration;
import me.ups3t.socket.Listener;

public class CHTTP {

    public static void main(String[] args) {

        Configuration configuration = Configuration.getConfig();

        ProgramUtils.checkWebRoot();
        Listener.getInstance().startListening(configuration.getPort());

    }

}
