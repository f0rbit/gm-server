package dev.forbit.server.abstracts;

import dev.forbit.server.interfaces.ConnectionServer;
import lombok.Getter;
import lombok.Setter;

public abstract class TCPServer extends Thread implements ConnectionServer {
    @Getter @Setter String address;

    @Getter @Setter int port;

    @Getter @Setter boolean running;

}
