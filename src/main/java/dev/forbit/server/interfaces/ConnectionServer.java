package dev.forbit.server.interfaces;

import com.google.gson.GsonBuilder;
import dev.forbit.server.abstracts.Server;
import dev.forbit.server.exceptions.ServerInitialisationError;

/**
 * Defines common methods across Connection Servers
 * see {@link dev.forbit.server.abstracts.TCPServer} and {@link dev.forbit.server.abstracts.UDPServer}
 */
public interface ConnectionServer {

    /**
     * Start the server
     */
    void start();

    /**
     * Get the port
     *
     * @return port to host server on
     */
    int getPort();

    /**
     * Gets the parent {@link Server} object that the packet was received on
     *
     * @return Server that packet was received on
     */
    Server getServer();

    /**
     * Gets the address to host the bind the server too.
     *
     * @return Address to host on.
     */
    String getAddress();

    /**
     * Sets running to false
     */
    default void shutdown() {
        setRunning(false);
    }

    /**
     * Returns whether the server is currently running
     *
     * @return true if running
     */
    boolean isRunning();

    /**
     * Sets the server running status
     *
     * @param running running status
     */
    void setRunning(boolean running);

    /**
     * Initialise the server.
     *
     * @return false if an error occured
     */
    boolean init();

    /**
     * The basic loop that happens every tick
     */
    void loop();

    /**
     * Initialises the server and begins the loop
     *
     * @throws ServerInitialisationError throws if there was an error in the initialisation phase.
     */
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

    /**
     * Returns a GSON serialised string of the object
     *
     * @return GSON string
     */
    default String getString() {
        var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return this.getClass().getName() + "" + gson.toJson(this);
    }
}
