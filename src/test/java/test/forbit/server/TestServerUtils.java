package test.forbit.server;


import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.ConnectionPacket;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PingPacket;
import dev.forbit.server.packets.RegisterPacket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class TestServerUtils {


    @Test void shouldReturnStringSimple() {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        String string = "Test String.. 1 2 3";
        buffer.put(string.getBytes());
        buffer.put((byte) 0x00); // all strings from gamemaker are null terminated so we need to simulate this.
        buffer.flip();
        buffer.rewind();
        try {
            Assertions.assertEquals(ServerUtils.getNextString(buffer), string);
        } catch (IOException e) {
            Assertions.fail("Threw IO Exception");
            e.printStackTrace();
        }

    }

    @Test void shouldReturnStringComplex() {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        String[] strings = {"   This is the first string.!7827$%$*(@&{}{ASD>?]", "{{{[[.//\"\nSecond string\t\"\")"};
        buffer.put(strings[0].getBytes());
        buffer.put((byte) 0x00);
        buffer.put(strings[1].getBytes());
        buffer.put((byte) 0x00);
        buffer.flip();
        buffer.rewind();
        try {
            Assertions.assertEquals(ServerUtils.getNextString(buffer), strings[0]);
        } catch (IOException e) {
            Assertions.fail("Threw IO Exception");
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(ServerUtils.getNextString(buffer), strings[1]);
        } catch (IOException e) {
            Assertions.fail("Threw IO Exception");
            e.printStackTrace();
        }
    }

    @Test void shouldReturnBuffer() {
        Random random = new Random();
        byte[] bytes = new byte[25];
        random.nextBytes(bytes);


        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(b);
        }
        String byteString = builder.toString();

        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.put(bytes);
        buffer.flip();
        buffer.rewind();

        String string = ServerUtils.getBuffer(buffer);
        Assertions.assertEquals(string, byteString);

    }

    @Test void testPackets() {
        Assertions.assertAll(() -> { Assertions.assertTrue(packetTest(PingPacket.class)); },
                             () -> { Assertions.assertTrue(packetTest(ConnectionPacket.class)); },
                             () -> { Assertions.assertTrue(packetTest(RegisterPacket.class)); }


        );
    }

    boolean packetTest(Class<? extends Packet> clazz) {
        Packet packet = null;
        try {
            packet = ServerUtils.getPacket(clazz.getName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Assertions.fail("Get Packet threw exception.");
            e.printStackTrace();
        }
        return (packet.getClass().equals(clazz));
    }


}
