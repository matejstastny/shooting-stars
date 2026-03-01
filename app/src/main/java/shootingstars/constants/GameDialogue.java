/*
 * Author: Matej Stastny
 * Date created: 6/15/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import java.util.ArrayList;

import shootingstars.tools.SpreadsheetUtil;

public class GameDialogue {

    private static final String SPREADSHEET_FILENAME = "dialogue";
    private static final String SPREADSHEET_NAME = "dialogue";

    private static int currLanguageIndex = 0;

    // ---- Language name ----
    public static String languageName;
    // ---- Fonts ----
    public static String headingFont;
    public static String textFont;
    // ---- Gloabl ----
    public static String languageDisplayName;
    public static String appName;
    // ---- Main menu ----
    public static String menuSubText;
    public static String deleteDataButton;
    public static String instagram;
    public static String github;
    // ---- Game Widgets ----
    public static String score;
    public static String topscore;
    public static String timeLeft;
    // ---- Game over screen ----
    public static String gameOver;
    public static String gameOverSubtext;
    // ---- Pause screen ----
    public static String pause;

    // Modifiers -----------------------------------------------------------------

    /**
     * Set's the current language to the next one on the language list. If the index
     * is higher that the total language count, it will be set to zero to cycle the
     * process.
     */
    public static void setNextLanguage() {
        int languageCount = getLanguages().size();
        currLanguageIndex++;
        if (currLanguageIndex > languageCount - 1) {
            currLanguageIndex = 0;
        }
        changeLanguage(currLanguageIndex);
    }

    /**
     * Set's the current language to the previous one on the language list. If the
     * index is smaller than 0, it will be set to the last idex of the language list
     * to cycle the prosess.
     */
    public static void setPreviousLanguage() {
        int languageCount = getLanguages().size();
        currLanguageIndex--;
        if (currLanguageIndex < 0) {
            currLanguageIndex = languageCount - 1;
        }
        changeLanguage(currLanguageIndex);
    }

    /**
     * Sets the language. Used in the initial process of setting up the last
     * language used. For invalide language, input -1 and the method will set the
     * language to the first one on the language list. If the index is out of bounds
     * of the language list, it will also be set to the first language on the list.
     *
     * @param index - language index.
     */
    public static void initialLanguageSet(int index) {
        if (index < 0 || index > getLanguages().size() - 1) {
            index = 0;
        }
        changeLanguage(index);
    }

    // Getters -------------------------------------------------------------------

    /**
     * Gets all of the available languages from the dialogue spreadsheet.
     *
     * @return an {@code ArrayList} of strings with names of the languages.
     */
    public static ArrayList<String> getLanguages() {
        ArrayList<String> languages = SpreadsheetUtil.getRowValues(SPREADSHEET_FILENAME, SPREADSHEET_NAME, 0);
        languages.remove(0);
        return languages;
    }

    /**
     * Accesor method for the current language index.
     *
     * @return and {@code int} of the index.
     */
    public static int getLanguageIndex() {
        return currLanguageIndex;
    }

    // Private -------------------------------------------------------------------

    private static void changeLanguage(int languageIndex) {

        // ---- Set variables ---
        currLanguageIndex = languageIndex;

        // ---- Change dialogue variables ----
        ArrayList<String> values = SpreadsheetUtil.getColumnValues(SPREADSHEET_FILENAME, SPREADSHEET_NAME, currLanguageIndex);
        String[] result = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = SpreadsheetUtil.getCellValue(SPREADSHEET_FILENAME, SPREADSHEET_NAME, i, languageIndex + 1);
        }

        languageName = result[0];
        headingFont = result[1];
        textFont = result[2];
        languageDisplayName = result[3];
        appName = result[4];
        menuSubText = result[5];
        score = result[6];
        topscore = result[7];
        timeLeft = result[8];
        gameOver = result[9];
        gameOverSubtext = result[10];
        pause = result[11];
        deleteDataButton = result[12];
        instagram = result[13];
        github = result[14];
    }

}
