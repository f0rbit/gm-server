package dev.forbit.server.scheduler;

import lombok.Getter;
import lombok.Setter;

public class RepeatingTask implements Task {
    @Getter final int period;
    @Getter final Runnable action;
    @Getter @Setter int delay;
    @Getter boolean running = true;

    public RepeatingTask(int period, int delay, Runnable action) {
        this.period = period;
        this.delay = delay;
        this.action = action;
    }

    @Override
    public boolean tick() {
        if (getDelay() <= 0) {
            getAction().run();
            setDelay(getPeriod());
        } else {
            setDelay(getDelay() - 1);
        }
        return isRunning();
    }

    public void stop() {
        running = false;
    }
}
