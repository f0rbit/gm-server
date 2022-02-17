package dev.forbit.resources;

import lombok.Data;

@Data
public class ClientAction {
    MockClient client;
    Runnable runnable;

    public void run() {
        getRunnable().run();
    }
}
