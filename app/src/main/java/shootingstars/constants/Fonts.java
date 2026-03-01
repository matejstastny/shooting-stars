/*
 * Author: Matej Stastny
 * Date created: 5/16/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {

    public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);

    public static Font headingFont = DEFAULT_FONT;
    public static Font textFont = DEFAULT_FONT;

    // Modifiers -----------------------------------------------------------------

    private static Font setFont(String fontType) {
        if (fontType.equals("defaultFont")) {
            return DEFAULT_FONT;
        }
        InputStream fontStream = getFontInputStream("/fonts/" + fontType);
        try {
            return Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch (FontFormatException | IOException e) {
            return DEFAULT_FONT;
        }
    }

    public static void setFonts() {
        setHeadingFont(GameDialogue.headingFont);
        setTextFont(GameDialogue.textFont);
    }

    // Getters -------------------------------------------------------------------

    public static Font heading() {
        return headingFont;
    }

    public static Font text() {
        return textFont;
    }

    // Private -------------------------------------------------------------------

    private static InputStream getFontInputStream(String fontName) {
        InputStream resource = Fonts.class.getResourceAsStream(fontName);
        if (resource == null) {
            System.out.println("Input stream is null for font: " + fontName);
        }
        return resource;
    }

    private static void setHeadingFont(String fileName) {
        headingFont = setFont(fileName);
    }

    private static void setTextFont(String fileName) {
        textFont = setFont(fileName);
    }

}
