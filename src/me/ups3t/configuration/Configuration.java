package me.ups3t.configuration;

import me.ups3t.logger.LogType;
import me.ups3t.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class Configuration {

    private File configFile;
    private File webRoot;
    private Map<String, Object> configMap;
    private static Configuration instance;

    private Configuration() {
        String filePath = "/opt/chttp.json";
        configFile = new File(filePath);
        if(!configFile.exists()) {
            Logger.alert(LogType.INFO, "Configuration file (" + filePath + ") does not exist, creating now");
            try {
                if (!configFile.createNewFile()) {
                    Logger.alert(LogType.CRITICAL, "Could not create configuration file (" + filePath + ")");
                    System.exit(1);
                }else{
                    Logger.alert(LogType.INFO, "Created configuration file (" + filePath + ")");
                }
            }catch (IOException exception) {
                Logger.alert(LogType.CRITICAL, "Could not create configuration file (" + filePath + ")");
                System.exit(1);
            }


        }

        if(!configFile.canRead()) {
            Logger.alert(LogType.CRITICAL, "Configuration file (" + filePath + ") exists, but is not readable.");
            System.exit(1);
        }

        BufferedReader file_in = null;

        try {
            file_in = new BufferedReader(new FileReader(configFile));
        } catch (FileNotFoundException ex) {
            Logger.alert(LogType.CRITICAL, "Could not open requested file! Stack trace for nerds:\n" + ex.getMessage());
        }

        StringBuilder configJson = new StringBuilder();
        String line = "";

        try {
            while ((line = file_in.readLine()) != null)
                configJson.append(line);
        } catch (IOException ex) {

        }

        JSONObject json = new JSONObject(configJson.toString());
        configMap = jsonToMap(json);

        if(!getWebRootFile().isDirectory() || !getWebRootFile().canRead() || !getWebRootFile().exists()) {
            Logger.alert(LogType.CRITICAL, "Cannot access web directory (" + getWebRoot() + "), either the folder does not exist or the program does not have access to it.");
        }

        File defaultPage = new File(getWebRoot(), getDefaultPage());
        if(!defaultPage.exists() || !defaultPage.canRead()) {
            Logger.alert(LogType.CRITICAL, "Cannot access web file (" + getDefaultPage() + "), either the folder does not exist or the program does not have access to it.");
            System.exit(1);
        }

        File notFoundPage = new File(getWebRoot(), getNotFoundPage());
        if(!notFoundPage.exists() || !notFoundPage.canRead()) {
            Logger.alert(LogType.CRITICAL, "Cannot access web file (" + getNotFoundPage() + "), either the folder does not exist or the program does not have access to it.");
            System.exit(1);
        }


    }

    public static Configuration getConfig() {
        if(instance == null) instance = new Configuration();
        return instance;
    }

    public File getWebRootFile() {
        if(webRoot == null) webRoot = new File(getWebRoot());
        return webRoot;

    }

    public String getWebRoot() {
        return (String) configMap.get("webroot");
    }

    public int getPort() {
        return Integer.parseInt((String)configMap.get("port"));
    }

    public String getNotFoundPage() {
        return (String) configMap.get("page_not_found");
    }

    public String getDefaultPage() {
        return (String) configMap.get("default_page");
    }

    // [!] Credit to https://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap [!]


    public Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != null) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keySet().iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

}
