/*
 * Author: Matej Stastny
 * Date created: 5/9/2024
 * Github link:  https://github.com/matejstastny/shooting-stars
 */

package shootingstars.common;

import java.util.Timer;
import java.util.TimerTask;

public class PausableTimer {

    private Timer timer;
    private long delay;
    private boolean isPaused;
    private boolean isRunning;
    private int executions;
    private int currentExecution;
    private Runnable onFinishTask;
    private Runnable onExecution;

    // Constructor ---------------------------------------------------------------

    /**
     * Default contructor. Sets the given parameters.
     * </p>
     * DOESN'T AUTOMATICALLY START THE TIMER!
     *
     * @param delay
     * @param executions
     * @param onFinishTask
     * @param onExecution
     */
    public PausableTimer(long delay, int executions, Runnable onFinishTask, Runnable onExecution) {
        this.delay = delay;
        this.timer = new Timer();
        this.isPaused = false;
        this.isRunning = false;
        this.executions = executions;
        this.currentExecution = 0;
        this.onFinishTask = onFinishTask;
        this.onExecution = onExecution;
    }

    // Conntrolls ----------------------------------------------------------------

    public void start() {
        if (!isRunning) {
            isRunning = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        onExecution.run();
                        currentExecution++;
                        if (currentExecution == executions) {
                            stop();
                        }
                    }
                }
            }, 0, delay);
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void stop() {
        timer.cancel();
        isRunning = false;
        if (onFinishTask != null) {
            onFinishTask.run();
        }
    }

    public void forceStop() {
        timer.cancel();
        isRunning = false;
    }

    // Accesors ------------------------------------------------------------------

    public long getTimeRemaining() {
        if (!isRunning) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        long nextExecutionTime = currentTime + (executions - currentExecution - 1) * delay;
        return nextExecutionTime - currentTime;
    }

}
