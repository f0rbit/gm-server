package dev.forbit.server.scheduler;

import dev.forbit.server.instances.ServerInterface;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Scheduler extends Thread {
    @Getter HashSet<Task> tasks;
    boolean running = false;
    ServerInterface server;

    public Scheduler(ServerInterface server) {
        this.server = server;
        this.tasks = new HashSet<>();
    }

    @Override public void run() {
        //System.out.println("[SCHEDULER] Started scheduler");
        server.getLogger().info("Started Scheduler.");
        running = true;
        long lastTick = System.currentTimeMillis() - 100;
        while (running) {
            if (System.currentTimeMillis() - lastTick < 100) { continue; }
            List<Task> remove = new ArrayList<>();
            HashSet<Task> copy = (new HashSet<>());
            copy.addAll(getTasks());
            for (Task task : copy) {
                if (!task.tick()) {
                    remove.add(task);
                }
            }
            remove.forEach(getTasks()::remove);
            lastTick = System.currentTimeMillis();
        }
    }

    public void stopRunning() {
        running = false;
    }

    public void addTask(Task task) {
        tasks.add(task);

    }
}
