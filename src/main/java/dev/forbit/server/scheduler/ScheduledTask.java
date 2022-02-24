package dev.forbit.server.scheduler;

import lombok.Getter;

public class ScheduledTask implements Task {
    @Getter Runnable action;
    @Getter int delay;

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
