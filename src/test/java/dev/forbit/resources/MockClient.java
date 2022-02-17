package dev.forbit.resources;

import lombok.Getter;
import lombok.Setter;

public class MockClient {
    @Getter @Setter boolean connected;

    public MockClient() {
        // connect to TCP server
        setConnected(false);
        connectTCP();
    }

    private void connectTCP() {

    }
}
