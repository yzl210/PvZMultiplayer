package cn.leomc.pvzmultiplayer.client;

import cn.leomc.pvzmultiplayer.client.scene.Scene;
import cn.leomc.pvzmultiplayer.common.text.component.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SceneManager {

    private Scene currentScene;

    private final Queue<Runnable> tasksQueue = new ConcurrentLinkedQueue<>();


    public void setScene(Scene scene) {
        if (currentScene != null) {
            PvZMultiplayerClient.getInstance().getInputMultiplexer().removeProcessor(currentScene);
            currentScene.dispose();
        }
        currentScene = scene;
        if (currentScene != null) {
            PvZMultiplayerClient.getInstance().getInputMultiplexer().addProcessor(scene);
            currentScene.create();
        }
    }

    public String getTitle() {
        String title = Constants.TITLE.get();
        if (currentScene != null) {
            Component component = currentScene.getTitle();
            if (component != null)
                title += " | " + component.get();
        }
        return title;
    }

    public void render() {
        if (currentScene != null)
            currentScene.render();
    }

    public void tick() {
        while (!tasksQueue.isEmpty())
            tasksQueue.poll().run();

        if (currentScene != null)
            currentScene.tick();
    }

    public void runLater(Runnable runnable) {
        tasksQueue.add(runnable);
    }
}
