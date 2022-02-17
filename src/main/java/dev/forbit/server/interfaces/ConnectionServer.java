package dev.forbit.server.interfaces;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.utilities.Utilities;

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
            Utilities.getLogger().severe("Error in init phase. class" + getClass().getName());
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
