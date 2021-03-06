package dev.forbit.server.scheduler;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.utilities.Utilities;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Scheduler extends Thread {
    @Getter final Set<Task> tasks;
    final Server server;
    boolean running = false;

    public Scheduler(Server server) {
        this.server = server;
        this.tasks = new HashSet<>();
    }

    @Override
    public void run() {
        Utilities.getLogger().info("Started Scheduler.");
        running = true;
        long lastTick = System.currentTimeMillis() - 100;
        var remove = new HashSet<Task>();
        while (running) {
            if (System.currentTimeMillis() - lastTick < 100) { continue; }
            tasks.stream().filter(task -> !task.tick()).forEach(remove::add);
            lastTick = System.currentTimeMillis();
            if (remove.size() > 0) {
                tasks.removeAll(remove);
                remove.clear();
            }
        }
    }

    public void shutdown() {
        this.running = false;
    }

    public void addTask(Task t) {
        getTasks().add(t);
    }
}
