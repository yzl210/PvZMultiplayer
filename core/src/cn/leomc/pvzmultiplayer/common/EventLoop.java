package cn.leomc.pvzmultiplayer.common;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventLoop {

    private final Queue<Runnable> tasksQueue = new ConcurrentLinkedQueue<>();

    public void loop() {
        while (true) {
            long start = System.nanoTime();
            try {
                tick();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void runLater(Runnable runnable) {
        tasksQueue.add(runnable);
    }

    public Thread createThread(String name) {
        return new Thread(this::loop, name);
    }

}
