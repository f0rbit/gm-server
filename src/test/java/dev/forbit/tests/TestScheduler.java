package dev.forbit.tests;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.gson.GSONServer;
import dev.forbit.server.scheduler.RepeatingTask;
import dev.forbit.server.scheduler.ScheduledTask;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("6 - Test Scheduler")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestScheduler {
    static final String address = "localhost";
    static final int tcp_port = 28383;
    static final int udp_port = 28384;
    static Server server;

    @BeforeAll
    @SneakyThrows
    public static void setup() {
        var props = new ServerProperties(address, tcp_port, udp_port);
        server = new GSONServer(props);
        server.init();
        Thread.sleep(300L);
        Utilities.getLogger().info("Finished startup.");
    }

    @AfterAll
    public static void shutdownServer() { server.shutdown(); }

    @Test
    @Order(1)
    @DisplayName("Test Server is Running")
    public void testRunning() {
        assertTrue(server.getTCPServer().isRunning());
        assertTrue(server.getUDPServer().isRunning());
    }

    @Test
    @Order(2)
    @DisplayName("Test Repeating Task")
    public void testRepeatingTask() {
        final int[] count = {0};
        var task = new RepeatingTask(1, 0, new Runnable() {
            @Override
            public void run() {
                count[0]++;
            }
        });
        assertTrue(server.getScheduler().isPresent());
        server.getScheduler().get().addTask(task);
        /*Thread.sleep(1500L);
        assertTrue(count[0] >= 6);*/
        await().atMost(2, SECONDS).until(() -> count[0] >= 6);
        task.stop();
        assertTrue(true);
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Test Scheduler timings")
    @ValueSource(ints = {300, 600, 900, 1200})
    public void testSchedulerTimings(int time) {
        final int[] count = {0};
        var task = new RepeatingTask(1, 0, new Runnable() {
            @Override
            public void run() {
                count[0]++;
            }
        });
        assertTrue(server.getScheduler().isPresent());
        server.getScheduler().get().addTask(task);
        await().atMost(time, MILLISECONDS).until(() -> count[0] >= (time / 200));
        task.stop();
        assertTrue(true);
    }

    @Test
    @Order(4)
    @DisplayName("Test Delayed Task")
    public void testDelayedTask() {
        assertTrue(server.getScheduler().isPresent());
        final int[] count = {0};
        var task = new ScheduledTask(4, () -> count[0]++);
        server.getScheduler().get().addTask(task);
        await().atMost(1500, MILLISECONDS).until(() -> count[0] == 1);
        assertEquals(count[0], 1);
    }

}
