package me.ups3t;

import me.ups3t.configuration.Configuration;
import me.ups3t.logger.LogType;
import me.ups3t.logger.Logger;

import java.io.File;

public class ProgramUtils {

    public static final String PROGRAM_NAME = "CHTTP";
    public static final double PROGRAM_VERSION = 0.1;

    public static final boolean BETA = true;
    public static final String PROGRAM_INFO = PROGRAM_NAME + " version " + PROGRAM_VERSION + (BETA ? " (beta)" : "");

    public static void checkWebRoot() {

        Configuration config = Configuration.getConfig();

        if (!config.getWebRootFile().exists()) {

            Logger.alert(LogType.INFO, config.getWebRootFile().getPath() + " directory does not exist, creating now");

            if (config.getWebRootFile().mkdir()) {
                Logger.alert(LogType.INFO, "Successfully created " + config.getWebRootFile().getPath());
            } else {
                Logger.alert(LogType.CRITICAL, config.getWebRootFile().getPath() + " could not be created.");
                System.exit(1);
            }
        }

        if (!(config.getWebRootFile().canRead())) {

            Logger.alert(LogType.CRITICAL, config.getWebRootFile().getPath() + " has no reading permissions!");
            Logger.alert(LogType.CRITICAL, "Please add the required privileges and start the program.");

            System.exit(1);

        }
    }

}
