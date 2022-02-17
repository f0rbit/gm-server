package dev.forbit.server.utilities;

import lombok.Data;

@Data
public class ServerProperties {
    String address;

    int tcpPort;

    int udpPort;
}
