package dev.forbit.server.networks.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;

public abstract class GSONPacket extends Packet {
    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        buffer.writeString(gson.toJson(this));
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        // dont use this method.
    }
}
