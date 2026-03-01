/*
 * Author: Matej Stastny
 * Date created: 6/14/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.menu;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import shootingstars.constants.Colors;
import shootingstars.constants.Fonts;
import shootingstars.constants.GameDialogue;
import shootingstars.constants.WidgetTags;
import shootingstars.constants.ZIndexes;
import shootingstars.interfaces.Renderable;
import shootingstars.tools.FontUtil;

/**
 * Main menu screen, shown to player with the game launch.
 *
 */
public class MenuScreen implements Renderable {

    /////////////////
    // Constants
    ////////////////

    private final Color MAIN_TEXT_COLOR = Colors.MAIN_TEXT;
    private final Color SUBTEXT_COLOR = Colors.SUB_TEXT;

    /////////////////
    // Variables
    ////////////////

    private int[] size;
    private boolean isVisible;

    /////////////////
    // Constructor
    ////////////////

    /**
     * Default constructor.
     *
     * @param size - size of the game over screen, commonly the size of the owning
     *             {@code JPanel}.
     */
    public MenuScreen(int[] size) {
        this.size = size;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {
        if (!this.isVisible) {
            return;
        }

        FontMetrics fm;
        int[] testPos;
        int x;
        int y;
        int sideTextOffset;
        String mainMessage = GameDialogue.appName;
        String subMessage = GameDialogue.menuSubText;

        // Paints the main message
        g.setColor(this.MAIN_TEXT_COLOR);
        g.setFont(Fonts.heading().deriveFont(Font.PLAIN, 80));
        fm = g.getFontMetrics();
        testPos = FontUtil.getCenteredPos(this.size[0], this.size[1], fm, mainMessage);
        x = testPos[0];
        y = testPos[1];
        sideTextOffset = fm.getHeight();
        g.drawString(mainMessage, x, y);

        // Paints the smaller bottom message
        g.setColor(SUBTEXT_COLOR);
        g.setFont(Fonts.heading().deriveFont(Font.PLAIN, 40));
        fm = g.getFontMetrics();
        testPos = FontUtil.getCenteredPos(this.size[0], this.size[1], fm, subMessage);
        x = testPos[0];
        y = testPos[1];
        g.drawString(subMessage, x, y + sideTextOffset);
    }

    @Override
    public int getZIndex() {
        return ZIndexes.SCREENS;
    }

    @Override
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void hide() {
        this.isVisible = false;
    }

    @Override
    public void show() {
        this.isVisible = true;
    }

    @Override
    public ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add(WidgetTags.MAIN_MENU);
        return tags;
    }

}
