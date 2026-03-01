/*
 * Author: Matej Stastny
 * Date created: 6/18/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.menu.settings_panel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import shootingstars.constants.Fonts;
import shootingstars.constants.GameDialogue;
import shootingstars.constants.Interact;
import shootingstars.constants.WidgetTags;
import shootingstars.constants.ZIndexes;
import shootingstars.interfaces.Interactable;
import shootingstars.interfaces.Renderable;
import shootingstars.tools.FontUtil;

/**
 * Widget to display the current game language.
 *
 */
public class DeleteData implements Renderable, Interactable {

    /////////////////
    // Constants
    ////////////////

    public static final int[] size = { 400, 80 };
    private static final int rounded = 50;
    private static final int borderHeight = 10;

    /////////////////
    // Variables
    ////////////////

    private int[] position;
    private boolean isVisible;

    /////////////////
    // Constructors
    ////////////////

    /**
     * Default constructor.
     *
     * @param pos - position of the widget.
     */
    public DeleteData(int[] pos) {
        this.position = pos;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {
        if (!this.isVisible) {
            return;
        }

        String text = GameDialogue.deleteDataButton;

        g.setColor(Color.BLACK);
        g.fillRoundRect(this.position[0] - borderHeight / 2, this.position[1] - borderHeight / 2, size[0] + borderHeight, size[1] + borderHeight, rounded + borderHeight, rounded + borderHeight);

        g.setColor(Color.RED);
        g.fillRoundRect(this.position[0], this.position[1], size[0], size[1], rounded, rounded);
        g.setFont(Fonts.text().deriveFont(Font.PLAIN, size[1] - 30));
        g.setColor(Color.BLACK);
        int[] pos = FontUtil.getCenteredPos(size[0], size[1], g.getFontMetrics(), text);
        g.drawString(text, this.position[0] + pos[0], this.position[1] + pos[1]);
    }

    @Override
    public int getZIndex() {
        return ZIndexes.POPUP_PANEL_BUTTONS;
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
        tags.add(WidgetTags.OPTIONS);
        return tags;
    }

    @Override
    public boolean wasInteracted(MouseEvent e) {
        Rectangle r = new Rectangle(this.position[0] - borderHeight / 2, this.position[1] - borderHeight / 2, size[0] + borderHeight, size[1] + borderHeight);
        return r.contains(e.getPoint());
    }

    @Override
    public Runnable getInteraction() {
        return Interact.DELETE_DATA;
    }

}
