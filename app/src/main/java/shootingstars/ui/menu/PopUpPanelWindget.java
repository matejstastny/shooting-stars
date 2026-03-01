/*
 * Author: Matej Stastny
 * Date created: 6/16/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.ui.menu;

import java.awt.Color;
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
 * Menu render, it's empty by default and needs to be overwriten.
 *
 */
public class PopUpPanelWindget implements Renderable, Interactable {

    /////////////////
    // Constants
    ////////////////

    private final double SCALE_COEF = 0.8;
    private final int EDGE_COEF = 80;
    private final int BORDER_THICKNES = 10;
    private final Color BLUR = new Color(255, 255, 255, 100);
    private final int CLOSE_BTN_RADIUS = 30;
    private final int CLOSE_BTN_BORDER_OFSET = 10;
    private final int ICON_PADDING = 15;
    private final int BUTTON_BORDER_THICKNES = 5;

    /////////////////
    // Variables
    ////////////////

    private int[] size;
    private int[] position;
    private int[] screenSize;
    private boolean isVisible;

    /////////////////
    // Constructors
    ////////////////

    /**
     * Default constructor.
     *
     * @param owner - owning {@code JPanel} object.
     */
    public PopUpPanelWindget(int[] screenSize) {
        this.screenSize = screenSize;
        this.size = new int[2];
        this.position = new int[2];
        this.size[0] = (int) (screenSize[0] * SCALE_COEF);
        this.size[1] = (int) (screenSize[1] * SCALE_COEF);
        this.position[0] = (screenSize[0] - this.size[0]) / 2;
        this.position[1] = (screenSize[1] - this.size[1]) / 2;
    }

    /////////////////
    // Render
    ////////////////

    @Override
    public void render(Graphics2D g, Container img) {

        if (!this.isVisible) {
            return;
        }

        // Blur fill
        g.setColor(BLUR);
        g.fillRect(0, 0, this.screenSize[0], this.screenSize[1]);

        int x = this.position[0] - BORDER_THICKNES / 2;
        int y = this.position[1] - BORDER_THICKNES / 2;
        int width = this.size[0] + BORDER_THICKNES;
        int height = this.size[1] + BORDER_THICKNES;

        // Panel outline
        g.setColor(Colors.MAIN_GRAY);
        g.fillRoundRect(x, y, width, height, EDGE_COEF + BORDER_THICKNES, EDGE_COEF + BORDER_THICKNES);

        // Panel window
        g.setColor(Colors.MAIN_GREEN);
        g.fillRoundRect(this.position[0], this.position[1], this.size[0], this.size[1], EDGE_COEF, EDGE_COEF);

        x = this.position[0] + this.size[0] - CLOSE_BTN_RADIUS * 2 - CLOSE_BTN_BORDER_OFSET;
        y = this.position[1] + CLOSE_BTN_BORDER_OFSET;
        width = CLOSE_BTN_RADIUS * 2;
        height = CLOSE_BTN_RADIUS * 2;

        // Exit icon outline
        g.setColor(Colors.MAIN_GRAY);
        g.fillOval(x, y, height, width);

        // Exit icon
        g.setColor(Colors.MAIN_GREEN);
        g.fillOval(x + BUTTON_BORDER_THICKNES, y + BUTTON_BORDER_THICKNES, width - BUTTON_BORDER_THICKNES * 2, height - BUTTON_BORDER_THICKNES * 2);

        // Exit icon texture
        Image icon = ImageUtil.scaleImage(Textures.CLOSE_ICON, width - ICON_PADDING * 2, height - ICON_PADDING * 2);
        g.drawImage(icon, x + ICON_PADDING, y + ICON_PADDING, img);
    }

    @Override
    public int getZIndex() {
        return ZIndexes.POPUP_PANEL;
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
        tags.add(WidgetTags.LINKS);
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
        int x = e.getX();
        int y = e.getY();
        int posX = this.position[0] + this.size[0] - CLOSE_BTN_RADIUS * 2 - CLOSE_BTN_BORDER_OFSET + CLOSE_BTN_RADIUS + BUTTON_BORDER_THICKNES / 2;
        int posY = this.position[1] + CLOSE_BTN_BORDER_OFSET + CLOSE_BTN_RADIUS + BUTTON_BORDER_THICKNES / 2;

        return Math.abs(posX - x) <= CLOSE_BTN_RADIUS + BUTTON_BORDER_THICKNES / 2 && Math.abs(posY - y) <= CLOSE_BTN_RADIUS + BUTTON_BORDER_THICKNES / 2;
    }

}
