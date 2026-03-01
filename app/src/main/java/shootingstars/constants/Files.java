/*
 * Author: Matej Stastny
 * Date created: 5/16/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import java.io.File;
import java.io.IOException;

import shootingstars.App;

public class Files {

    public static final String TOP_SCORE_FILENAME = "topscore.JSON";
    private static final String LOG_FILENAME = "log.txt";
    private static final String USER_DATA_DIR_NAME = "user_data";
    private static final String LOG_DIRECTORY_NAME = "system_logs";
    private static final String SETTINGS_FILE_NAME = "settings.JSON";

    public static final String DATA_DIR = getAppDataDirectory(App.APP_NAME).getAbsolutePath();
    public static final String USER_DATA_DIR = DATA_DIR + File.separator + USER_DATA_DIR_NAME;
    public static final String LOG_DIR = DATA_DIR + File.separator + LOG_DIRECTORY_NAME;
    public static final String RESOURCE_DIR = getResourcesDir();
    public static final String FONT_DIR = getFontsDir();

    public static final File TOP_SCORE_FILE = getScoreFile();
    public static final File LOG_FILE = getLogFile();
    public static final File USER_CONFIG_FILE = getSettingsFile();

    private static String getFileName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callerClassName = stackTrace[2].getClassName();
        String fileName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1) + ".java";
        return fileName;
    }

    private static String getProjectFolderPath() {
        String fileName = getFileName();
        int nameLenght = fileName.length();
        File folder = new File(fileName);
        String absolutePath = folder.getAbsolutePath();
        int pathLenght = absolutePath.length();
        return absolutePath.substring(0, pathLenght - nameLenght - 1);
    }

    private static String getResourcesDir() {
        return getProjectFolderPath() + File.separator + "src" + File.separator + "main" + File.separator + "Resources";
    }

    private static String getFontsDir() {
        return getResourcesDir() + File.separator + "Fonts";
    }

    private static File getAppDataDirectory(String appName) {
        String osName = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        File appDataDir = null;

        // Picks a path dependion on the OS
        if (osName.contains("win")) {
            // Windows: C:\Users\<user>\AppData\Roaming\<appName>
            String appData = System.getenv("APPDATA");
            appDataDir = new File(appData, appName);
        } else if (osName.contains("mac")) {
            // macOS: /Users/<user>/Library/Application Support/<appName>
            appDataDir = new File(userHome, "Library/Application Support/" + appName);
        } else if (osName.contains("nix") || osName.contains("nux")) {
            // Linux: /home/<user>/.<appName>
            appDataDir = new File(userHome, "." + appName);
        }

        // Ensure the directory exists, and creates it if it doesn't
        if (appDataDir != null && !appDataDir.exists()) {
            appDataDir.mkdirs();
        }

        return appDataDir;
    }

    private static File getScoreFile() {
        File userDataDir = new File(USER_DATA_DIR);

        // Checks if the dir exists, and creates it if it doesn't
        if (!userDataDir.exists()) {
            userDataDir.mkdir();
        }

        File scoreFile = new File(USER_DATA_DIR + File.separator + TOP_SCORE_FILENAME);
        try {
            if (!userDataDir.exists()) {
                userDataDir.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "FATAL - Could not create a score save file";
        }

        return scoreFile;
    }

    private static File getLogFile() {
        File logDir = new File(LOG_DIR);

        // Checks if the dir exists, and creates it if it doesn't
        if (!logDir.exists()) {
            logDir.mkdir();
        }

        File logFile = new File(LOG_DIR + File.separator + LOG_FILENAME);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            assert false : "FATAL - Could not create a log file";
        }

        return logFile;
    }

    private static File getSettingsFile() {
        File settingsDir = new File(USER_DATA_DIR);

        // Checks if the dir exists, and creates it if it doesn't
        if (!settingsDir.exists()) {
            settingsDir.mkdir();
        }

        File settingsFile = new File(USER_DATA_DIR + File.separator + SETTINGS_FILE_NAME);
        try {
            if (!settingsFile.exists()) {
                settingsFile.createNewFile();
            }
        } catch (IOException e) {
            assert false : "FATAL - Could not create a settings file";
        }

        return settingsFile;
    }

}
