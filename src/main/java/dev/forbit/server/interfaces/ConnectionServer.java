package dev.forbit.server.interfaces;

import dev.forbit.server.abstracts.Server;

public interface ConnectionServer {

    void start();

    int getPort();

    Server getServer();

    String getAddress();

    default void shutdown() {
        setRunning(false);
    }

    boolean isRunning();

    void setRunning(boolean running);

    boolean init();

    void loop();

    default void begin() {
        if (!(init())) {
            System.out.println("ERROR IN INIT PHASE. CLASS: " + getClass().getName());
            setRunning(false);
            return;
        }

        setRunning(true);
        while (isRunning()) {
            loop();
        }

        shutdown();
    }
}
