/*
 * Author: Matej Stastny
 * Date created: 6/15/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Textures {

    public static final Image STAR = setImage("star.png");
    public static final Image LINK_ICON = setImage("link_icon.png");
    public static final Image SETTINGS_ICON = setImage("settings_icon.png");
    public static final Image CLOSE_ICON = setImage("close_icon.png");
    public static final Image INSTAGRAM_LOGO = setImage("instagram_logo.png");
    public static final Image GITHUB_LOGO = setImage("github_logo.png");
    public static final Image ARROW_RIGHT = setImage("arrow_right.png");
    public static final Image ARROW_LEFT = setImage("arrow_left.png");
    public static final Image HOME_ICON = setImage("home_icon.png");
    public static final Image ICON = setImage("icon.png");

    private static Image setImage(String imageName) {
        String path = "/textures/" + imageName;
        InputStream imageStream = getImageInputStream(path);
        if (imageStream == null) {
            return null;
        }
        BufferedImage img;
        try {
            img = ImageIO.read(imageStream);
        } catch (IOException e) {
            return null;
        }
        return img;
    }

    private static InputStream getImageInputStream(String imageName) {
        InputStream resource = Textures.class.getResourceAsStream(imageName);
        if (resource == null) {
            System.out.println("Input stream is null for texture: " + imageName);
        }
        return resource;
    }

}
