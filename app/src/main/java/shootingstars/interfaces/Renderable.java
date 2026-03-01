/*
 * Author: Matej Stastny
 * Date created: 7/27/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.interfaces;

import java.awt.Container;
import java.awt.Graphics2D;
import java.util.ArrayList;

public interface Renderable {

    /**
     * This method renders the object using a {@code Graphics2D} object reference.
     *
     * @param g - {@code Graphocs2D} object reference of the target {@code JPanel}
     *          object.
     */
    public void render(Graphics2D g, Container focusCycleRootAncestor);

    /**
     * This method returns the Z-Index of the object. The Z-Index cannot be changed!
     *
     * @return {@code int} value of the Z-Index.
     */
    public int getZIndex();

    /**
     * This method returns if the item is hidden or shown, therefore should be
     * rendered or not.
     *
     * @return {@code boolean} visible/hidden.
     */
    public boolean isVisible();

    /**
     * Hides the element = prevents it from being rendered.
     *
     */
    public void hide();

    /**
     * Shows the element = allows rendering of the element
     *
     */
    public void show();

    /**
     * Accesor for the tags of the element. Tags are used to filrer UI elements into
     * categories.
     *
     * @return - {@code ArrayList} of tags.
     */
    public ArrayList<String> getTags();

}
