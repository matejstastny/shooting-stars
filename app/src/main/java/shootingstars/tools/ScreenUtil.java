/*
 * Author: Matej Stastny
 * Date created: 6/13/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.tools;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenUtil {

    /**
     * Calculates the desired size of the window depening on the dimensions of the
     * user screen.
     *
     * @param dimentions - user's screen dimensions.
     * @return {@code double} position array.
     */
    public static double[] getAppWindowSize() {
        int[] screenDimentions = getScreenDimensions();
        double x = screenDimentions[0] * 0.9;
        double y = screenDimentions[1] * 0.9;
        double[] dimensions = { x, y };
        return dimensions;
    }

    public static int[] getScreenDimensions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int[] dimensions = { screenWidth, screenHeight };
        return dimensions;
    }

}
