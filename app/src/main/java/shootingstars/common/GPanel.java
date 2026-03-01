/*
 * Author: Matej Stastny
 * Date created: 7/23/2024
 * Github link:  https://github.com/matejstastny/shooting-stars
 */

package shootingstars.common;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import shootingstars.App;
import shootingstars.interfaces.Interactable;
import shootingstars.interfaces.Renderable;

/**
 * <h2>GPanel</h2> {@code GPanel} is a custom {@code JPanel} object that handles
 * rendering of graphical elements using an internal Renderer, running on a
 * separate thread. It integrates with a {@code JFrame} to manage window
 * properties.
 * </p>
 * <hr>
 * <br>
 * <h3>Constructor</h3> {@code GPanel} will be upon costruction set with these
 * values:
 * <ul>
 * <li><b>FPS</b> - interval, at which will the renderer calculate new
 * frames</li>
 * <li><b>Window width & window height</b> - dimensions of the
 * {@code JFrame}</li>
 * <li><b>Resizable</b> - makes the window fixed size or resizable by user</li>
 * <li><b>App title</b> - text displayed on the {@code JFrame} as the app
 * title</li>
 * </ul>
 * The {@code JFrame} will be put in the middle of the user screen by default.
 * It will also have the default icon, that can be changed separatly by using
 * the {@code setIcon()} method. If the project is being packaged, change the
 * style the icon is being accesed. The app will be made visible, and the
 * rendering prosess will start.
 * </p>
 * <hr>
 * <h3>UI Elements</h3> This class supports adding renderable objects that are
 * drawn in a layered order based on their {@code z-index}. The object must
 * implement the interface {@code Renderable}. This is how the objects are
 * added:
 *
 * <pre>
 * <code>
 * public void add(Renderable renderable);
 * </code>
 * </pre>
 *
 * </p>
 * <hr>
 * <h3>Rendering</h3> The rendering loop can be controlled with {@code start()}
 * and {@code stop()} methods.
 * </p>
 * The Renderer class inside GPanel controls the rendering loop, adjusting its
 * interval based on the provided frames-per-second value.
 * </p>
 * <hr>
 * <h3>Action listeners</h3> This class implements {@codeMouseListener} and
 * {@code MouseMotionListener} to handle mouse interaction events.
 * Implementation of these methods should be handeled by the user of this class,
 * depending on the project needs.
 * <hr>
 *
 * @author Matěj Šťastný
 * @since 7/23/2024
 */
public class GPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private JFrame appFrame;
    private Renderer renderer;
    private boolean isRendering;
    private ArrayList<Renderable> widgets;

    // Constructor ---------------------------------------------------------------

    public GPanel(int fps, int windowWidth, int windowHeight, boolean resizable, String appTitle) {
        // ---- Variable Initiliazition ----
        this.widgets = new ArrayList<Renderable>();
        this.appFrame = new JFrame();
        this.renderer = new Renderer(fps);
        this.isRendering = false;

        // ---- JFrame setup ----
        this.appFrame.setSize(windowWidth, windowHeight);
        this.appFrame.setResizable(resizable);
        this.appFrame.setTitle(appTitle);
        this.appFrame.setLocationRelativeTo(null);
        this.appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.appFrame.setVisible(true);
        this.appFrame.add(this);

        // ---- Action listeners setup ----
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        requestFocusInWindow();

        // ---- Start rendering ----
        startRendering();

    }

    // Rendering controll --------------------------------------------------------

    public void startRendering() {
        if (!this.isRendering) {
            this.renderer.start();
            this.isRendering = true;
        }
    }

    public void stopRendering() {
        if (this.isRendering) {
            this.renderer.stop();
            this.isRendering = false;
        }
    }

    // Rendering -----------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        // ------------------------------------------------

        for (int i = 0; i < this.widgets.size(); i++) {
            Renderable renderable = this.widgets.get(i);
            if (renderable.isVisible()) {
                renderable.render(g, this.appFrame.getFocusCycleRootAncestor());
            }
        }
    }

    /*
     * Size methods return the measurments of the owning {@JFrame} object. Methods
     * return zero, if owner wasn't initialized (is {@code null}).
     */

    @Override
    public int getWidth() {
        return this.appFrame == null ? 0 : this.appFrame.getWidth();
    }

    @Override
    public int getHeight() {
        return this.appFrame == null ? 0 : this.appFrame.getHeight();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        App.app.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        App.app.keyReleased(e);
    }

    // Getters -------------------------------------------------------------------

    public JFrame getAppFrame() {
        return this.appFrame;
    }

    public <T> ArrayList<T> getWidgetsByClass(Class<T> targetClass) {
        ArrayList<T> list = new ArrayList<>();
        for (Renderable r : this.widgets) {
            if (targetClass.isInstance(r)) {
                list.add(targetClass.cast(r));
            }
        }
        return list;
    }

    public ArrayList<Renderable> getInteractables() {
        ArrayList<Renderable> interactables = new ArrayList<Renderable>();
        for (Renderable r : this.widgets) {
            if (r instanceof Interactable) {
                interactables.add(r);
            }
        }
        return interactables;
    }

    // Modifiers -----------------------------------------------------------------

    public void add(Renderable renderable) {
        int value = renderable.getZIndex();
        int i = 0;

        // Find the correct position to insert the value
        while (i < this.widgets.size() && value < this.widgets.get(i).getZIndex()) {
            i++;
        }

        // Insert the value at the found position
        this.widgets.add(i, renderable);
    }

    public void add(List<Renderable> widgets) {
        for (Renderable r : widgets) {
            this.add(r);
        }
    }

    public void setIcon(Image icon) {
        Taskbar taskbar = Taskbar.getTaskbar();
        try {
            taskbar.setIconImage(icon);
        } catch (UnsupportedOperationException e) {
            // Fallback for Windows
            this.appFrame.setIconImage(icon);
        }
    }

    // Widget visibility ---------------------------------------------------------

    public void hide(int index) {
        if (index >= 0 && index < this.widgets.size() - 1) {
            this.widgets.get(index).hide();
        }
    }

    public void show(int index) {
        if (index >= 0 && index < this.widgets.size() - 1) {
            this.widgets.get(index).show();
        }
    }

    public void hideAllWidgets() {
        for (Renderable r : this.widgets) {
            r.hide();
        }
    }

    public void showAllWidgets() {
        for (Renderable r : this.widgets) {
            r.show();
        }
    }

    public void hideTaggedWidgets(String tag) {
        for (Renderable r : this.widgets) {
            for (String currTag : r.getTags()) {
                if (currTag.equals(tag)) {
                    r.hide();
                }
            }
        }
    }

    public void showTaggedWidgets(String tag) {
        for (Renderable r : this.widgets) {
            for (String currTag : r.getTags()) {
                if (currTag.equals(tag)) {
                    r.show();
                }
            }
        }
    }

    // Rendering engine ----------------------------------------------------------

    private class Renderer implements Runnable {

        private boolean running = false;
        private int targetFPS;

        public Renderer(int fps) {
            setFps(fps);
        }

        public void start() {
            running = true;
            Thread renderThread = new Thread(this, "Render Thread");
            renderThread.start();
        }

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {

                long optimalTime = 1000000000 / targetFPS; // In nanoseconds
                long startTime = System.nanoTime();

                render();

                long elapsedTime = System.nanoTime() - startTime;
                long sleepTime = optimalTime - elapsedTime;

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime / 1000000, (int) (sleepTime % 1000000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void render() {
            SwingUtilities.invokeLater(() -> {
                GPanel.this.repaint();
            });
        }

        public void setFps(int value) {
            targetFPS = value;
        }
    }

    // Interaction ---------------------------------------------------------------

    @Override
    public void mouseDragged(MouseEvent e) {
        App.app.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        App.app.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        App.app.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        App.app.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        App.app.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        App.app.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        App.app.mouseExited(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        App.app.keyTyped(e);
    }

}
