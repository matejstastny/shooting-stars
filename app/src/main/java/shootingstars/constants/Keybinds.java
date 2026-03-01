/*
 * Author: Matej Stastny
 * Date created: 5/16/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keybinds {

    private static ArrayList<Keybind> keybinds;
    private static boolean initialized = false;

    public static final String START_KEY = "start";
    public static final String PAUSE_KEY = "pause";
    public static final String RESTART_KEY = "restart";
    public static final String DEBUGG_KEY = "debug";

    public static void setKeybinds() {
        keybinds = new ArrayList<Keybind>();
        keybinds.add(new Keybind(KeyEvent.VK_S, START_KEY, Interact.START));
        keybinds.add(new Keybind(KeyEvent.VK_ESCAPE, PAUSE_KEY, Interact.PAUSE));
        keybinds.add(new Keybind(KeyEvent.VK_R, RESTART_KEY, Interact.RESTART));
        keybinds.add(new Keybind(KeyEvent.VK_X, DEBUGG_KEY, Interact.DEBUG));
    }

    public static void interact(KeyEvent k) {
        if (!initialized) {
            setKeybinds();
            initialized = true;
        }

        for (Keybind kb : keybinds) {
            if (kb.getEnabled() && kb.getKeybind() == k.getKeyCode()) {
                kb.getInteract().run();
            }
        }
    }

    public static void setEnabled(boolean value, String name) {
        if (!initialized) {
            setKeybinds();
            initialized = true;
        }
        for (Keybind k : keybinds) {
            if (k.getDisplaName().equals(name)) {
                k.setEnabled(value);
            }
        }
    }

    public static void setEnabledAll(boolean value) {
        if (!initialized) {
            setKeybinds();
            initialized = true;
        }
        for (Keybind k : keybinds) {
            k.setEnabled(value);
        }
    }

    public static class Keybind {

        private int keybind;
        private String displayName;
        private boolean enabled;
        private Runnable interact;

        public Keybind(int keybind, String displayName, Runnable interact) {
            this.keybind = keybind;
            this.displayName = displayName;
            this.enabled = false;
            this.interact = interact;
        }

        public void setEnabled(boolean value) {
            this.enabled = value;
        }

        public Runnable getInteract() {
            return this.interact;
        }

        public int getKeybind() {
            return this.keybind;
        }

        public boolean getEnabled() {
            return this.enabled;
        }

        public String getDisplaName() {
            return this.displayName;
        }

    }

}
