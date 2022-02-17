package dev.forbit.server.utilities;

import lombok.Data;

@Data
public class ServerProperties {
    String address;

    int tcpPort;

    int udpPort;

    public ServerProperties(String address, int tcp_port, int udp_port) {
        setAddress(address);
        setTcpPort(tcp_port);
        setUdpPort(udp_port);
    }
}
