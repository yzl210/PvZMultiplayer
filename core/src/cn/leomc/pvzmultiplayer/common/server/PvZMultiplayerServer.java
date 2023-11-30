package cn.leomc.pvzmultiplayer.common.server;

import cn.leomc.pvzmultiplayer.common.Constants;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PvZMultiplayerServer {

    private static PvZMultiplayerServer INSTANCE;

    private final Queue<Runnable> tasksQueue = new ConcurrentLinkedQueue<>();

    public static void start() {
        if (INSTANCE != null)
            throw new IllegalStateException("Server is already running!");

        INSTANCE = new PvZMultiplayerServer();

        new Thread(() -> {
            try {
                INSTANCE.loop();
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(-1);
            }
        }, "Server Thread").start();

    }

    public void loop() {
        while (true) {
            long start = System.nanoTime();
            tick();
            long end = System.nanoTime();
            long sleep = 1000000000 / Constants.TPS - (end - start);
            while (System.nanoTime() - end < sleep)
                Thread.onSpinWait();
        }
    }

    public void tick() {
        while (!tasksQueue.isEmpty())
            tasksQueue.poll().run();
    }

}
