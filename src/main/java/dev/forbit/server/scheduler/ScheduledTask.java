package dev.forbit.server.scheduler;

import lombok.Getter;
import lombok.Setter;

public class ScheduledTask implements Task {
    @Getter @Setter Runnable action;
    @Getter @Setter int delay;

    public ScheduledTask(int delay, Runnable action) {
        setDelay(delay);
        setAction(action);
    }

    @Override
    public boolean tick() {
        if (getDelay() <= 0) {
            getAction().run();
            return false;
        } else {
            this.delay = getDelay() - 1;
            return true;
        }
    }
}
