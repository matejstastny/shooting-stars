/*
 * Author: Matej Stastny
 * Date created: 6/16/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.menu;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import shootingstars.constants.Colors;
import shootingstars.constants.WidgetTags;
import shootingstars.constants.ZIndexes;
import shootingstars.interfaces.Interactable;
import shootingstars.interfaces.Renderable;
import shootingstars.tools.ImageUtil;

/**
 * Button with the menu button style.
 *
 */
public class MenuButton implements Renderable, Interactable {

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
    private Runnable interaction;
    private boolean isVisible;
    private boolean disabledInteract;

    /////////////////
    // Constructors
    ////////////////

    /**
     * Default button constructor.
     *
     * @param pos   - position of the button.
     * @param owner - owning {@code JPanel} object.
     */
    public MenuButton(int[] pos, Image texture, Runnable interaction) {
        this.texture = texture;
        this.position = pos;
        this.interaction = interaction;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {
        if (!isVisible) {
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
        return ZIndexes.MENU_BUTTONS;
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

    /////////////////
    // Interact
    ////////////////

    @Override
    public Runnable getInteraction() {
        return this.interaction;
    }

    @Override
    public boolean wasInteracted(MouseEvent e) {
        if (this.disabledInteract) {
            return false;
        }

        int x = e.getX() - position[0];
        int y = e.getY() - position[1];
        return x <= SIZE[0] && y <= SIZE[1] && x > 0 && y > 0;
    }

    /////////////////
    // Modifiers
    ////////////////

    /**
     * Button texture modifier. Sets the icon image displayed in the middle of the
     * button.
     *
     * @param texture - new {@code Image} texture.
     */
    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public void setInteract(boolean value) {
        this.disabledInteract = !value;
    }

}
