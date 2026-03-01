/*
 * Author: Matej Stastny
 * Date created: 9/11/2024
 * Github link: https://github.com/matejstastny/shooting-stars
 */

package shootingstars.constants;

import shootingstars.App;
import shootingstars.common.Links;

/**
 * Constants class with all possbile events that can be triggered by user
 * interaction. These events are then referenced in objects with the
 * {@code Interactable} interface
 *
 */
public class Interact {

    public static final Runnable HOME = () -> {
        App.app.onGoToMenu();
    };

    public static final Runnable OPTIONS = () -> {
        App.app.onGoToOptions();
    };

    public static final Runnable LINKS = () -> {
        App.app.onGoToLinks();
    };

    public static final Runnable GITHUB = () -> {
        Links.openURL(Links.GITHUB);
    };

    public static final Runnable INSTAGRAM = () -> {
        Links.openURL(Links.INSTAGRAM);
    };

    public static final Runnable NEXT_LAN = () -> {
        App.app.onLanguageChange(true);
    };

    public static final Runnable PREV_LAN = () -> {
        App.app.onLanguageChange(false);
    };

    public static final Runnable PAUSE = () -> {
        App.app.onTogglePause();
    };

    public static final Runnable DEBUG = () -> {

    };

    public static final Runnable RESTART = () -> {
        App.app.onGameRestart();
    };

    public static final Runnable START = () -> {
        App.app.onGameStart();
    };

    public static final Runnable TARGET_INTERACTED = () -> {
        App.app.onTargetHit(false);
    };

    public static final Runnable DELETE_DATA = () -> {
        App.app.onDeleteData();
    };

}
