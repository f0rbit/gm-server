package dev.forbit.server.interfaces;

public interface ConnectionServer {

    void start();

    int getPort();

    String getAddress();

    void shutdown();

    boolean isRunning();
}
