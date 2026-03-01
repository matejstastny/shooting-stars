/*
 * Author: Matej Stastny
 * Date created: 6/15/2024
 * Github link:  https://github.com/matejstastny/shooting-stars
 */

package shootingstars.common;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import shootingstars.constants.Logs;

public class Links {

    public static String INSTAGRAM = "https://www.instagram.com/my_daarlin";
    public static String GITHUB = "https://github.com/matejstastny/shooting-stars";

    public static void openURL(String url) {
        try {
            URI uri = new URI(url);
            Desktop desktop = Desktop.getDesktop();
            if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            } else {
                Logs.log("ERROR - Internet browsing not supported!");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
