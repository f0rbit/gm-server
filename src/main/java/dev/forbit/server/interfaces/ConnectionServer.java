package dev.forbit.server.interfaces;

import com.google.gson.GsonBuilder;
import dev.forbit.server.abstracts.Server;
import dev.forbit.server.exceptions.ServerInitialisationError;

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

    default void begin() throws ServerInitialisationError {
        if (!(init())) {
            //Utilities.getLogger().severe("Error in init phase. class" + getClass().getName());
            setRunning(false);
            throw new ServerInitialisationError();
        }

        setRunning(true);
        while (isRunning()) {
            loop();
        }

        shutdown();
    }

    default String getString() {
        var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return this.getClass().getName() + "" + gson.toJson(this);
    }
}
