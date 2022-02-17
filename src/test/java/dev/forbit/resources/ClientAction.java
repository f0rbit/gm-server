package dev.forbit.resources;

import dev.forbit.server.utilities.GMLInputBuffer;
import lombok.Data;

@Data
public abstract class ClientAction {
    MockClient client;
    GMLInputBuffer buffer;

    public abstract void run();
}
