package dev.forbit.server.scheduler;

import lombok.Getter;
import lombok.Setter;

public class ScheduledTask implements Task {

    @Getter @Setter int executeTime;
    @Getter @Setter Runnable task;

    public ScheduledTask(Runnable runnable, int delay) {
        setTask(runnable);
        setExecuteTime(delay);
    }

    @Override public boolean tick() {
        if (getExecuteTime() <= 0) {
            System.out.println("Executed task");
            getTask().run();
            return false;
        }
        else {
            setExecuteTime(getExecuteTime() - 1);
            return true;
        }
    }

}
