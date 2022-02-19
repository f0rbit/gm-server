package dev.forbit.resources;

import dev.forbit.server.utilities.GMLInputBuffer;
import lombok.Data;

@Data
public abstract class ClientAction {
    RawMockClient client;
    GMLInputBuffer buffer;

    public abstract void run();
}
