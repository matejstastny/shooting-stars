/*
 * Author: Matej Stastny
 * Date created: 7/3/2024
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
import shootingstars.tools.FontUtil;

/**
 * Scoreboard widget for the Game Over screen. Shows the top score and the score
 * in the current run.
 *
 */
public class ScoreBoard implements Renderable {

    /////////////////
    // Constants
    ////////////////

    private final int[] SIZE = { 400, 200 };
    private final int BORDER_WIDTH = 10;
    private final int ARC_WIDTH = 40;
    private final int BETWEEN_TEXT = 20;

    /////////////////
    // Variables
    ////////////////

    private int[] position;
    private int topScore;
    private int score;
    private boolean visible;

    /////////////////
    // Constructor
    ////////////////

    public ScoreBoard(int[] position) {
        this.position = position;
        this.topScore = -1;
        this.score = 1;
        this.visible = false;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {

        if (!visible) {
            return;
        }

        String topMessage = GameDialogue.topscore + ": " + this.topScore;
        String bottomMessage = GameDialogue.score + ": " + this.score;

        g.setColor(Colors.MAIN_GRAY);
        g.fillRoundRect(this.position[0] - BORDER_WIDTH / 2, this.position[1] - BORDER_WIDTH / 2, SIZE[0] + BORDER_WIDTH, SIZE[1] + BORDER_WIDTH, ARC_WIDTH + BORDER_WIDTH, ARC_WIDTH + BORDER_WIDTH);
        g.setColor(Colors.MAIN_GREEN);
        g.fillRoundRect(this.position[0], this.position[1], SIZE[0], SIZE[1], ARC_WIDTH, ARC_WIDTH);

        g.setFont(Fonts.text().deriveFont(Font.BOLD, 30));
        g.setColor(Color.BLACK);

        int[] topCentered = FontUtil.getCenteredPos(SIZE[0], SIZE[1], g.getFontMetrics(), topMessage);
        int[] bottomCentered = FontUtil.getCenteredPos(SIZE[0], SIZE[1], g.getFontMetrics(), bottomMessage);

        // Calculate the vertical positions for the two messages
        int topMessageY = topCentered[1] - g.getFontMetrics().getHeight() / 2 - BETWEEN_TEXT / 2 + position[1];
        int bottomMessageY = bottomCentered[1] + g.getFontMetrics().getHeight() / 2 + BETWEEN_TEXT / 2 + position[1];

        // Draw the strings
        g.drawString(topMessage, topCentered[0] + position[0], topMessageY);
        g.drawString(bottomMessage, bottomCentered[0] + position[0], bottomMessageY);

    }

    @Override
    public int getZIndex() {
        return ZIndexes.SCREENS;
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
        tags.add(WidgetTags.GAME_OVER);
        return tags;
    }

    /////////////////
    // Score value setters
    ////////////////

    /**
     * Setter for the topscore value.
     *
     * @param topScore - new topscore value;
     */
    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    /**
     * Setter for the score value.
     *
     * @param score - new score value.
     */
    public void setScore(int score) {
        this.score = score;
    }

}
