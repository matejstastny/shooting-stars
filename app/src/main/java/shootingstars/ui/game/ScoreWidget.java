/*
 * Author: Matej Stastny
 * Date created: 6/13/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import shootingstars.constants.Colors;
import shootingstars.constants.Fonts;
import shootingstars.constants.GameDialogue;
import shootingstars.constants.WidgetTags;
import shootingstars.constants.ZIndexes;
import shootingstars.interfaces.Renderable;

/**
 * Widget, that displays score at the top of the screen.
 *
 */
public class ScoreWidget implements Renderable {

    /////////////////
    // Constants
    ////////////////

    private final Color BACKROUND_COLOR = Colors.MAIN_GREEN;
    private final Color TEXT_COLOR = Colors.WIDGET_TEXT;

    /////////////////
    // Fields
    ////////////////

    private int currScore;
    private int[] position;
    private boolean visible;

    /////////////////
    // Contructor
    ////////////////

    public ScoreWidget(int[] position) {
        this.currScore = 0;
        this.position = position;
    }

    /////////////////
    // Render methods
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {

        if (!visible) {
            return;
        }

        Font font = Fonts.text();

        g.setColor(BACKROUND_COLOR);
        g.fillRoundRect(this.position[0], this.position[1], 250, 50, 20, 20);
        g.setColor(TEXT_COLOR);
        g.setFont(font.deriveFont(Font.BOLD, 24));
        g.drawString(GameDialogue.score + ": " + this.currScore, this.position[0] + 15, this.position[1] + 24 + 8);
    }

    @Override
    public int getZIndex() {
        return ZIndexes.GAME_WIDGETS;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void hide() {
        this.visible = false;
    }

    @Override
    public void show() {
        this.visible = true;
    }

    @Override
    public ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add(WidgetTags.GAME);
        return tags;
    }

    /////////////////
    // Modifiers
    ////////////////

    /**
     * Modifier method for the score.
     *
     * @param score - new score value.
     */
    public void setScore(int score) {
        this.currScore = score;
    }

}
