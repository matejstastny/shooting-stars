/*
 * Author: Matej Stastny
 * Date created: 6/14/2024
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
 * Widget to display players top score.
 *
 */
public class TopscoreWidget implements Renderable {

    /////////////////
    // Constants
    ////////////////

    private final Color TIMER_BACKROUND = Colors.SCORES_WIDGETS;
    private final Color TIMER_TEXT = Colors.WIDGET_TEXT;

    /////////////////
    // Variables
    ////////////////

    private int topscore;
    private int[] position;
    private boolean visible;

    /////////////////
    // Constructors
    ////////////////

    /**
     * Deafult contructor with a default widget topscore value.
     *
     * @param position - position of the widget.
     * @param topscore - default topscore value.
     */
    public TopscoreWidget(int[] position, int topscore) {
        this.topscore = topscore;
        this.position = position;
    }

    /**
     * Constructor without a default topscore value given, is set to 0.
     *
     * @param position - position of the widget.
     */
    public TopscoreWidget(int[] position) {
        this(position, 0);
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
        g.setColor(TIMER_BACKROUND);
        g.fillRoundRect(this.position[0], this.position[1], 250, 50, 20, 20);
        g.setColor(TIMER_TEXT);
        g.setFont(font.deriveFont(Font.BOLD, 24));
        g.drawString(GameDialogue.topscore + ": " + this.topscore, this.position[0] + 15, this.position[1] + 24 + 8);
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
     * Modifier method for the time.
     *
     * @param topscore - new value.
     */
    public void setTopscore(int topscore) {
        this.topscore = topscore;
    }

}
