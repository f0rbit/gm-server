package dev.forbit.server.scheduler;

import lombok.Getter;
import lombok.Setter;

public class RepeatingTask implements Task {

    @Getter @Setter int delay;
    @Getter @Setter int period;
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
        }
        else {
            setDelay(getDelay() - 1);
        }
        return true;
    }

}
