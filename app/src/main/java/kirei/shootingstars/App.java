/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 6/13/2024
 * Github link:  https://github.com/kireiiiiiiii/shooting-stars
 */

package kirei.shootingstars;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import kirei.shootingstars.common.AdvancedVariable;
import kirei.shootingstars.common.GPanel;
import kirei.shootingstars.common.PausableTimer;
import kirei.shootingstars.common.Settings;
import kirei.shootingstars.constants.Colors;
import kirei.shootingstars.constants.Files;
import kirei.shootingstars.constants.Fonts;
import kirei.shootingstars.constants.GameDialogue;
import kirei.shootingstars.constants.Interact;
import kirei.shootingstars.constants.Keybinds;
import kirei.shootingstars.constants.Logs;
import kirei.shootingstars.constants.Textures;
import kirei.shootingstars.constants.WidgetTags;
import kirei.shootingstars.interfaces.Interactable;
import kirei.shootingstars.interfaces.Renderable;
import kirei.shootingstars.tools.ScreenUtil;
import kirei.shootingstars.ui.Backround;
import kirei.shootingstars.ui.game.GameOverScreen;
import kirei.shootingstars.ui.game.HomeButton;
import kirei.shootingstars.ui.game.PauseScreen;
import kirei.shootingstars.ui.game.ScoreBoard;
import kirei.shootingstars.ui.game.ScoreWidget;
import kirei.shootingstars.ui.game.StarWidget;
import kirei.shootingstars.ui.game.TimerWidget;
import kirei.shootingstars.ui.game.TopscoreWidget;
import kirei.shootingstars.ui.menu.MenuButton;
import kirei.shootingstars.ui.menu.MenuScreen;
import kirei.shootingstars.ui.menu.PopUpPanelWindget;
import kirei.shootingstars.ui.menu.links_panel.GithubLink;
import kirei.shootingstars.ui.menu.links_panel.InstagramLink;
import kirei.shootingstars.ui.menu.settings_panel.ChangeButton;
import kirei.shootingstars.ui.menu.settings_panel.DeleteData;
import kirei.shootingstars.ui.menu.settings_panel.LanguageTitle;

public class App {

    public static final String APP_NAME = "ShootingStars";
    private static final int GAME_LENGHT = 30;
    private static final int DEFAULT_TARGET_RADIUS = 40;
    private static final int TARGET_SCORE = 10;
    private static final int FPS = 60;
    public final int WINDOW_PADDING = 10;

    public static App app;
    private GPanel gpanel;
    private AdvancedVariable<Integer> topScore;

    private PausableTimer timer;
    private int timeRemaining;
    private int targetRadius;
    private int score;
    private boolean paused;

    // Main ----------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }

    public App() {
        app = this;
        // ---- Log ----
        Logs.log(Logs.APP_START);
        // ---- Setup the GPanel ----
        double[] windowSize = ScreenUtil.getAppWindowSize();
        this.gpanel = new GPanel(FPS, (int) windowSize[0], (int) windowSize[1], false, "Shooting Stars");
        this.gpanel.setIcon(Textures.ICON);
        gpanel.getAppFrame().setBackground(Colors.BACKROUND);
        onUISetup();

        // ---- Load the score and options file ----
        onTopscoreFileLoad();
        GameDialogue.initialLanguageSet((int) Settings.getValue("languageIndex"));
        Fonts.setFonts();

        // ---- Display the menu elements ----
        onGoToMenu();
    }

    // Events --------------------------------------------------------------------

    public void onGoToMenu() {
        Keybinds.setEnabledAll(false);
        Keybinds.setEnabled(true, Keybinds.START_KEY);

        for (MenuButton w : this.gpanel.getWidgetsByClass(MenuButton.class)) {
            w.setInteract(true);
        }
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.MAIN_MENU);
    }

    public void onGoToOptions() {
        this.gpanel.getWidgetsByClass(DeleteData.class).forEach(DeleteData::show);
        for (MenuButton w : this.gpanel.getWidgetsByClass(MenuButton.class)) {
            w.setInteract(false);
        }
        this.gpanel.showTaggedWidgets(WidgetTags.OPTIONS);
    }

    public void onGoToLinks() {
        for (MenuButton w : this.gpanel.getWidgetsByClass(MenuButton.class)) {
            w.setInteract(false);
        }
        this.gpanel.showTaggedWidgets(WidgetTags.LINKS);
    }

    public void onGameStart() {
        // ---- Log & variable setup ----
        Logs.log(Logs.GAME_START);
        this.score = 0;
        this.paused = false;
        this.targetRadius = DEFAULT_TARGET_RADIUS;
        // ----Set keybinds ----
        Keybinds.setEnabledAll(false);
        Keybinds.setEnabled(true, Keybinds.PAUSE_KEY);
        Keybinds.setEnabled(true, Keybinds.RESTART_KEY);
        // ----Set widget values ----
        for (TopscoreWidget w : this.gpanel.getWidgetsByClass(TopscoreWidget.class)) {
            w.setTopscore(this.topScore.get());
        }
        for (ScoreWidget w : this.gpanel.getWidgetsByClass(ScoreWidget.class)) {
            w.setScore(this.score);
        }
        for (StarWidget w : this.gpanel.getWidgetsByClass(StarWidget.class)) {
            w.setRadius(this.targetRadius);
        }
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.GAME);
        // ---- Start the timer, and reset the target ----
        initializeTimer();
        onTargetHit(true);
    }

    /**
     * Restarts the game.
     *
     */
    public void onGameRestart() {
        // ---- Log & variable setup ----
        Logs.log(Logs.GAME_RESTART);
        this.score = 0;
        // ----Set keybinds ----
        Keybinds.setEnabledAll(false);
        Keybinds.setEnabled(true, Keybinds.PAUSE_KEY);
        Keybinds.setEnabled(true, Keybinds.RESTART_KEY);
        // ----Set widget values ----
        for (TopscoreWidget w : this.gpanel.getWidgetsByClass(TopscoreWidget.class)) {
            w.setTopscore(this.topScore.get());
        }
        for (ScoreWidget w : this.gpanel.getWidgetsByClass(ScoreWidget.class)) {
            w.setScore(this.score);
        }
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.GAME);
        // ---- Start the timer, and reset the target ----
        this.timer.forceStop();
        initializeTimer();
        onTargetHit(true);
    }

    public void onTogglePause() {
        if (paused) {
            onGameResumed();
        } else {
            onGamePause();
        }
        this.paused = !this.paused;
    }

    public void onGamePause() {
        // ---- Log ----
        Logs.log(Logs.GAME_PAUSE);
        // ----Set widget values ----
        Keybinds.setEnabled(false, Keybinds.RESTART_KEY);
        // ----Set widget values ----
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.PAUSE);
        // ---- Timer ----
        this.timer.pause();
    }

    public void onGameResumed() {
        // ---- Log ----
        Logs.log(Logs.GAME_RESUMED);
        // ----Set widget values ----
        Keybinds.setEnabled(true, Keybinds.RESTART_KEY);
        // ----Set widget values ----
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.GAME);
        // ---- Timer ----
        this.timer.resume();
    }

    public void onGameEnd() {
        // ---- Log ----
        Logs.log(Logs.GAME_OVER);
        // ----Set widget values ----
        Keybinds.setEnabled(false, Keybinds.PAUSE_KEY);
        // ----Set widget values ----
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.GAME_OVER);
        for (ScoreBoard w : this.gpanel.getWidgetsByClass(ScoreBoard.class)) {
            w.setScore(this.score);
            w.setTopScore(this.topScore.get());
        }
        this.gpanel.hideAllWidgets();
        this.gpanel.showTaggedWidgets(WidgetTags.GAME_OVER);
        // ---- Timer ----
        this.timer.forceStop();
        // ---- Check for new Topscore ----
        if (this.score > this.topScore.get()) {
            this.topScore.set(this.score);
            onTopscoreFileSave();
        }
        onTopscoreFileLoad();
    }

    public void onTimerIteration() {
        // ---- Log ----
        Logs.log(Logs.TIMER_ITERATION);
        // ----Set widget values ----
        for (TimerWidget w : this.gpanel.getWidgetsByClass(TimerWidget.class)) {
            w.setTime(timeRemaining);
        }
        this.timeRemaining--;
    }

    public void onTargetHit(boolean init) {
        // ---- Calculate next position ----
        int minY = DEFAULT_TARGET_RADIUS;
        int minX = DEFAULT_TARGET_RADIUS;
        int maxX = this.gpanel.getWidth() - DEFAULT_TARGET_RADIUS;
        int maxY = this.gpanel.getHeight() - DEFAULT_TARGET_RADIUS - 20;

        int x = (int) (Math.random() * (maxX - minX + 1)) + minX;
        int y = (int) (Math.random() * (maxY - minY + 1)) + minY;
        int[] pos = { x, y };

        // ----Set widget values ----
        for (StarWidget w : this.gpanel.getWidgetsByClass(StarWidget.class)) {
            w.setLocation(pos);
        }

        // ---- Update the score and log the click, if not ititial execution ----
        if (!init) {
            Logs.log(Logs.TARGET_HIT);
            this.score += TARGET_SCORE * 2;
            for (ScoreWidget w : this.gpanel.getWidgetsByClass(ScoreWidget.class)) {
                w.setScore(this.score);
            }
        }
    }

    public void onTopscoreFileLoad() {
        // ---- Log ----
        Logs.log(Logs.TOPSCORE_FILE_LOAD);
        // ---- Load topscore ----
        this.topScore = new AdvancedVariable<Integer>(Files.TOP_SCORE_FILE);
        try {
            this.topScore.loadFromFile(Integer.class);
        } catch (IOException e) {
            this.topScore.set(0);
        }
        // --- File empty ----
        if (this.topScore.get() == null) {
            this.topScore.set(0);
        }
    }

    public void onTopscoreFileSave() {
        // ---- Log ----
        Logs.log(Logs.TOPSCORE_FILE_SAVED);
        // --- Save file ----
        try {
            this.topScore.save();
        } catch (IOException e) {
            System.out.println("FATAL - Could not save Topscore file");
        }
    }

    public void onLanguageChange(boolean next) {
        // ---- Change dialogues ----
        if (next) {
            GameDialogue.setNextLanguage();
        } else {
            GameDialogue.setPreviousLanguage();
        }
        // ---- Change fonts ----
        Fonts.setFonts();
        // ---- Set app window title ----
        // this.gpanel.setName(GameDialogue.appName);
        // ---- Log ----
        Logs.log(Logs.LANGUAGE_SET);
        // ---- Save settings----
        Settings.save();
    }

    public void onMouseClicked(MouseEvent e) {
        ArrayList<Renderable> widgets = this.gpanel.getInteractables();
        // Clear not visible widgets
        for (int i = 0; i < widgets.size(); i++) {
            Renderable r = widgets.get(i);
            if (!r.isVisible()) {
                widgets.remove(i);
                i--;
            }
        }
        if (widgets.size() <= 0) {
            return;
        }

        // Sort based on Z-Index - user clicked the one displayed on top right? :3
        int size = widgets.size();
        boolean swapped;
        for (int i = 0; i < size - 1; i++) {
            swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                if (widgets.get(j).getZIndex() > widgets.get(j + 1).getZIndex()) {
                    // Swap the elements
                    Renderable temp = widgets.get(j);
                    widgets.set(j, widgets.get(j + 1));
                    widgets.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }

        // Convert to Interactable objects, and filter out all not clicked
        ArrayList<Interactable> buttons = new ArrayList<Interactable>();
        for (int i = 0; i < widgets.size(); i++) {
            Interactable btn = (Interactable) widgets.get(i);
            if (btn.wasInteracted(e)) {
                buttons.add(btn);
            }
        }
        if (buttons.size() > 0) {
            Interactable interacted = buttons.get(0);
            interacted.getInteraction().run();
        }

        // Update the score
        if (!paused) {
            this.score -= this.score <= 0 ? 0 : TARGET_SCORE;
            for (ScoreWidget w : this.gpanel.getWidgetsByClass(ScoreWidget.class)) {
                w.setScore(this.score);
            }
        }

    }

    public void onUISetup() {

        // Screen dimension for clarity
        int width = this.gpanel.getWidth();
        int height = this.gpanel.getHeight();

        // GLOBAL
        int[] appSize = { width, height };
        // MAIN MENU
        int[] menu_linkBtn = { width - 90, height - 110 };
        int[] menu_optionsBtn = { menu_linkBtn[0] - 100, menu_linkBtn[1] };
        // OPTIONS
        int[] options_instagramBtn = { width / 2 - 300, height / 2 - 50 };
        int[] options_gitBtn = { width / 2 + 130, height / 2 - 50 };
        int[] options_currlan = { width / 2 - LanguageTitle.size[0] / 2, height / 2 - LanguageTitle.size[1] / 2 };
        int[] options_currlan2 = { width / 2 - LanguageTitle.size[0] / 2, height - 220 };
        int[] options_prevlanBtn = { options_currlan[0] - ChangeButton.SIZE[0] - 20, options_currlan[1] };
        int[] options_nextlanBtn = { options_currlan[0] + LanguageTitle.size[0] + 20, options_currlan[1] };
        // GAME
        int[] game_score = { 20, 20 };
        int[] game_time = { 20, 80 };
        int[] game_topscore = { 20, 140 };
        // PAUSE
        int[] pause_menuBtn = { 20, 20 };
        // GAMEOVER
        int[] gameov_scoreboard = { 20, height - 240 };
        // ------------------------------------------------------------------------------------------------

        this.gpanel.add(Arrays.asList(
                // MAIN MENU
                (Renderable) new Backround(appSize), (Renderable) new MenuScreen(appSize), (Renderable) new MenuButton(menu_linkBtn, Textures.LINK_ICON, Interact.LINKS), // LINKS
                (Renderable) new MenuButton(menu_optionsBtn, Textures.SETTINGS_ICON, Interact.OPTIONS), // OPTIONS
                // OPTIONS & LINKS PANEL
                (Renderable) new PopUpPanelWindget(appSize), //
                (Renderable) new LanguageTitle(options_currlan), //
                (Renderable) new DeleteData(options_currlan2), //
                (Renderable) new ChangeButton(options_prevlanBtn, true), //
                (Renderable) new ChangeButton(options_nextlanBtn, false), //
                (Renderable) new InstagramLink(options_instagramBtn), //
                (Renderable) new GithubLink(options_gitBtn),
                // GAME MAIN
                (Renderable) new TimerWidget(game_time), (Renderable) new ScoreWidget(game_score), (Renderable) new TopscoreWidget(game_topscore), (Renderable) new StarWidget(),
                // PAUSE SCREEN
                (Renderable) new HomeButton(pause_menuBtn), (Renderable) new PauseScreen(appSize),
                // GAME OVER SCREEN
                (Renderable) new ScoreBoard(gameov_scoreboard), (Renderable) new GameOverScreen(appSize)));
    }

    public void onDeleteData() {
        this.topScore.set(0);
        onTopscoreFileSave();
        onTopscoreFileLoad();
        this.gpanel.getWidgetsByClass(DeleteData.class).forEach(DeleteData::hide);
    }

    // Interactions --------------------------------------------------------------

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        onMouseClicked(e);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        Keybinds.interact(e);
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    // Private -------------------------------------------------------------------

    private void initializeTimer() {
        if (this.timer != null) {
            timer.forceStop();
        }

        this.timeRemaining = GAME_LENGHT;

        Runnable onFinished = () -> {
            onGameEnd();
        };
        Runnable everyRun = () -> {
            onTimerIteration();
        };
        this.timer = new PausableTimer(1000, GAME_LENGHT + 1, onFinished, everyRun);
        timer.start();
    }

}
