package dev.forbit.server.old.scheduler;

/**
 * Interface with common methods among {@link ScheduledTask} and {@link RepeatingTask}
 */
public interface Task {
    /**
     * Does a tick of the task, there are 10 ticks in a second.
     * <p>
     * If the task is to be removed, this method should return false.
     *
     * @return whether or not this task should be removed from the execution stack.
     */
    boolean tick();

}
