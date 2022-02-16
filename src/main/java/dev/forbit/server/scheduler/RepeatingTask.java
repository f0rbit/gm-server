package dev.forbit.server.scheduler;

import lombok.Getter;
import lombok.Setter;

public class RepeatingTask implements Task {

    /**
     * The delay from when the task is registered to when it should start executing
     */
    @Getter @Setter int delay;

    /**
     * The period in which it should repeat
     */
    @Getter @Setter int period;

    /**
     * The task to repeat
     */
    @Getter @Setter Runnable task;

    public RepeatingTask(Runnable runnable, int period, int delay) {
        setTask(runnable);
        setDelay(delay);
        setPeriod(period);
    }

    @Override public boolean tick() {
        if (getDelay() < 0) {
            getTask().run();
            setDelay(getPeriod());
        } else {
            setDelay(getDelay() - 1);
        }
        return true;
    }

}
