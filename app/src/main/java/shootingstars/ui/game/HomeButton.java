/*
 * Author: Matej Stastny
 * Date created: 7/3/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.game;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import shootingstars.constants.Colors;
import shootingstars.constants.Interact;
import shootingstars.constants.Textures;
import shootingstars.constants.WidgetTags;
import shootingstars.constants.ZIndexes;
import shootingstars.interfaces.Interactable;
import shootingstars.interfaces.Renderable;
import shootingstars.tools.ImageUtil;

/**
 * Home button widget for the pause screen and the game over screen. Redirects
 * the user to the main menu.
 *
 */
public class HomeButton implements Renderable, Interactable {

    /////////////////
    // Constants
    ////////////////

    private final int[] SIZE = { 70, 70 };
    private final int[] ICON_PADDING = { 15, 15 };
    private final int BORDER_WIDTH = 10;
    private final int ARC_WIDTH = 40;

    /////////////////
    // Variables
    ////////////////

    private Image texture;
    private int[] position;
    private boolean visible;

    /////////////////
    // Constructor
    ////////////////

    /**
     * Default button constructor.
     *
     * @param owner    - owning {@code JPanel} object.
     * @param position - position of the button in the render.
     */
    public HomeButton(int[] position) {
        this.position = position;
        this.texture = Textures.HOME_ICON;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {

        if (!visible) {
            return;
        }

        g.setColor(Colors.MAIN_GRAY);
        g.fillRoundRect(this.position[0] - BORDER_WIDTH / 2, this.position[1] - BORDER_WIDTH / 2, SIZE[0] + BORDER_WIDTH, SIZE[1] + BORDER_WIDTH, ARC_WIDTH + BORDER_WIDTH, ARC_WIDTH + BORDER_WIDTH);
        g.setColor(Colors.MAIN_GREEN);
        g.fillRoundRect(this.position[0], this.position[1], SIZE[0], SIZE[1], ARC_WIDTH, ARC_WIDTH);

        if (this.texture != null) {
            g.drawImage(ImageUtil.scaleImage(this.texture, SIZE[0] - ICON_PADDING[0], SIZE[1] - ICON_PADDING[1]), position[0] + ICON_PADDING[0] / 2, position[1] + ICON_PADDING[1] / 2, img);
        }
    }

    @Override
    public int getZIndex() {
        return ZIndexes.WIDGETS;
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
        tags.add(WidgetTags.PAUSE);
        tags.add(WidgetTags.GAME_OVER);
        return tags;
    }

    /////////////////
    // Interact
    ////////////////

    @Override
    public Runnable getInteraction() {
        return Interact.HOME;
    }

    @Override
    public boolean wasInteracted(MouseEvent e) {
        int x = e.getX() - position[0];
        int y = e.getY() - position[1];
        return x <= SIZE[0] && y <= SIZE[1] && x > 0 && y > 0;
    }

}
