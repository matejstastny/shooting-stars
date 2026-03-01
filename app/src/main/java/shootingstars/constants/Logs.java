/*
 * Author: Matej Stastny
 * Date created: 6/15/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logs {

    // Logs ----------------------------------------------------------------------

    public static final String APP_START = "App start";

    public static final String GAME_START = "Game start";
    public static final String GAME_RESTART = "Game restart";
    public static final String GAME_OVER = "Game ended";
    public static final String GAME_PAUSE = "Game paused";
    public static final String GAME_RESUMED = "Game resumed";

    public static final String TARGET_HIT = "Target hit";
    public static final String TARGET_NOT_HIT = "Target not hit";

    public static final String TOPSCORE_FILE_LOAD = "\"" + Files.TOP_SCORE_FILENAME + "\" loaded";
    public static final String TOPSCORE_FILE_SAVED = "\"" + Files.TOP_SCORE_FILENAME + "\" saved";

    public static final String TIMER_ITERATION = "Timer executed";

    public static final String LANGUAGE_SET = "Language set to " + GameDialogue.languageName;

    // Color Mapping (Using ANSI colors instead of background colors) ------------

    private static final Map<String, String> LOG_COLORS = Map.ofEntries(Map.entry(APP_START, TerminalColors.RED), Map.entry(GAME_START, TerminalColors.GREEN), Map.entry(GAME_RESTART, TerminalColors.GREEN), Map.entry(GAME_OVER, TerminalColors.GREEN), Map.entry(GAME_PAUSE, TerminalColors.GREEN), Map.entry(GAME_RESUMED, TerminalColors.GREEN), Map.entry(TARGET_HIT, TerminalColors.BLUE), Map.entry(TARGET_NOT_HIT, TerminalColors.BLUE), Map.entry(TOPSCORE_FILE_LOAD, TerminalColors.PURPLE), Map.entry(TOPSCORE_FILE_SAVED, TerminalColors.PURPLE), Map.entry(TIMER_ITERATION, TerminalColors.YELLOW), Map.entry(LANGUAGE_SET, TerminalColors.WHITE));

    // Logging -------------------------------------------------------------------

    private static final List<String> logs = new ArrayList<>();
    private static final boolean USE_COLORS = true; // Set to false if you prefer plain text logs

    public static void log(String logInput) {
        String color = LOG_COLORS.getOrDefault(logInput, "");

        String logMessage = LocalTime.now() + " | " + logInput;
        logs.add(logMessage);

        if (USE_COLORS && !color.isEmpty()) {
            System.out.println(color + logMessage + TerminalColors.RESET);
        } else {
            System.out.println(logMessage);
        }

        try {
            listToFile(logs, Files.LOG_FILE);
        } catch (IOException e) {
            System.err.println("FATAL - could not save log file");
        }
    }

    // Log file ------------------------------------------------------------------

    /**
     * Writes the contents of a list into a file, clearing it beforehand. Each list
     * element is written on a separate line.
     *
     * @param list - List of log messages
     * @param file - Target file
     * @throws IOException If file operations fail
     */
    public static void listToFile(List<String> list, File file) throws IOException {
        java.nio.file.Files.write(file.toPath(), list, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
