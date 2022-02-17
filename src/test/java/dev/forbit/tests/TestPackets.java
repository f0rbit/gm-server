package dev.forbit.tests;

import dev.forbit.resources.MockPacket;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import dev.forbit.server.utilities.Utilities;
import org.junit.jupiter.api.*;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("2 - Test Packets")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPackets {

    MockPacket getMockPacket() {
        return new MockPacket(875, 132.02d, (short) 44, 181.23f, (byte) 87, false, "hello test world");
    }

    MockPacket getRandomMockPacket() {
        Random random = new Random();
        byte[] bytes = new byte[100];
        random.nextBytes(bytes);
        // prevent random NUL characters
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0x00) { bytes[i] = (byte) random.nextInt(Byte.MAX_VALUE); }
        }
        return new MockPacket(
                random.nextInt(),
                random.nextDouble(),
                (short) random.nextInt(Short.MAX_VALUE),
                random.nextFloat(),
                (byte) random.nextInt(Byte.MAX_VALUE),
                random.nextBoolean(),
                new String(bytes));
    }

    @Test
    @Order(1)
    @DisplayName("Test Packet Header")
    public void testPacketHeader() {
        ByteBuffer buffer = getMockPacket().getBuffer();
        Optional<String> header = Utilities.getNextString(buffer);
        assertTrue(header.isPresent());
        assertEquals(header.get(), "dev.forbit.resources.MockPacket");
    }

    @Test
    @Order(2)
    @DisplayName("Test Loading Buffer")
    public void testPacketInputStream() {
        var packet = getMockPacket();
        ByteBuffer buffer = packet.getBuffer();
        Optional<String> header = Utilities.getNextString(buffer);
        assertTrue(header.isPresent());
        // load into buffer
        GMLInputBuffer input = new GMLInputBuffer(buffer);
        MockPacket mockPacket = new MockPacket();
        mockPacket.loadBuffer(input);
        // assertions
        assertAll(
                () -> assertEquals(mockPacket.getIntValue(), 875),
                () -> assertEquals(mockPacket.getDoubleValue(), 132.02d),
                () -> assertEquals(mockPacket.getShortValue(), (short) 44),
                () -> assertEquals(mockPacket.getFloatValue(), 181.23f),
                () -> assertEquals(mockPacket.getByteValue(), (byte) 87),
                () -> assertFalse(mockPacket.isBooleanValue()),
                () -> assertEquals(mockPacket.getStringValue(), "hello test world"));
    }

    @Test
    @Order(3)
    @DisplayName("Test Writing Packet")
    public void testOutputBuffer() {
        var packet = getMockPacket();
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        packet.fillBuffer(buffer);
        ByteBuffer bytes = buffer.getBuffer();
        assertAll(
                () -> assertEquals(bytes.getInt(), 875),
                () -> assertEquals(bytes.getDouble(), 132.02d),
                () -> assertEquals(bytes.getShort(), (short) 44),
                () -> assertEquals(bytes.getFloat(), 181.23f),
                () -> assertEquals(bytes.get(), (byte) 87),
                () -> assertEquals(bytes.get(), 0),
                () -> {
                    Optional<String> string = Utilities.getNextString(bytes);
                    assertTrue(string.isPresent());
                    assertEquals(string.get(), "hello test world");
                });
    }

    @RepeatedTest(10)
    @DisplayName("Random Packet Information Tests")
    @Order(4)
    public void testRandomPackets() {
        var packet = getRandomMockPacket();
        ByteBuffer buffer = packet.getBuffer();
        // load from buffer
        Optional<String> header = Utilities.getNextString(buffer);
        assertTrue(header.isPresent());
        assertEquals(header.get(), "dev.forbit.resources.MockPacket");
        MockPacket mockPacket = new MockPacket();
        mockPacket.loadBuffer(new GMLInputBuffer(buffer));

        assertEquals(mockPacket.getIntValue(), packet.getIntValue());
        assertEquals(mockPacket.getDoubleValue(), packet.getDoubleValue());
        assertEquals(mockPacket.getShortValue(), packet.getShortValue());
        assertEquals(mockPacket.getByteValue(), packet.getByteValue());
        assertEquals(mockPacket.getShortValue(), packet.getShortValue());
        assertEquals(mockPacket.getStringValue(), packet.getStringValue());
        assertEquals(mockPacket.isBooleanValue(), packet.isBooleanValue());

    }
}
