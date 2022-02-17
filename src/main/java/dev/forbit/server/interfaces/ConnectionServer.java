package dev.forbit.server.interfaces;

public interface ConnectionServer {

    void start();

    int getPort();

    String getAddress();

    default void shutdown() {
        setRunning(false);
    }

    boolean isRunning();

    void setRunning(boolean running);

    void init();

    void loop();

    default void run() {
        init();

        while (isRunning()) {
            loop();
        }

        shutdown();
    }
}
