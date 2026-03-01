/*
 * Author: Matej Stastny
 * Date created: 6/16/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.menu.links_panel;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import shootingstars.constants.Colors;
import shootingstars.constants.Fonts;
import shootingstars.constants.GameDialogue;
import shootingstars.constants.Interact;
import shootingstars.constants.Textures;
import shootingstars.constants.WidgetTags;
import shootingstars.constants.ZIndexes;
import shootingstars.interfaces.Interactable;
import shootingstars.interfaces.Renderable;
import shootingstars.tools.ImageUtil;

/**
 * Button that openes my instagram account page in the browser.
 *
 */
public class InstagramLink implements Renderable, Interactable {

    /////////////////
    // Constants
    ////////////////

    private final int[] SIZE = { 90, 90 };

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
     * @param owner    - owning {@code JPanel} object.
     * @param position - position.
     */
    public InstagramLink(int[] position) {
        this.position = position;
        this.position[0] = this.position[0] + SIZE[0] / 2;
        this.position[1] = this.position[1] + SIZE[1] / 2;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {
        if (!this.isVisible) {
            return;
        }

        g.setColor(Colors.MAIN_GRAY);
        g.setFont(Fonts.text().deriveFont(Font.BOLD, 50));
        int textWidth = g.getFontMetrics().stringWidth(GameDialogue.instagram);
        int xPosition = position[0] + (SIZE[0] - textWidth) / 2;
        g.drawString(GameDialogue.instagram, xPosition, position[1] - 30);

        g.drawImage(ImageUtil.scaleImage(Textures.INSTAGRAM_LOGO, SIZE[0], SIZE[1]), position[0], position[1], img);
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
        tags.add(WidgetTags.LINKS);
        return tags;
    }

    /////////////////
    // Interact
    ////////////////

    @Override
    public Runnable getInteraction() {
        return Interact.INSTAGRAM;
    }

    @Override
    public boolean wasInteracted(MouseEvent e) {
        int x = e.getX() - position[0];
        int y = e.getY() - position[1];
        return x <= SIZE[0] && y <= SIZE[1] && x > 0 && y > 0;
    }

}
