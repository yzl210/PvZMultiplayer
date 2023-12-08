package cn.leomc.pvzmultiplayer.client;

import cn.leomc.pvzmultiplayer.client.scene.Scene;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneManager {

    private Scene currentScene;

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

    public Scene getCurrentScene() {
        return currentScene;
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
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        BitmapFont font = PvZMultiplayerClient.getInstance().getFont();
        batch.begin();
        font.getData().setScale(2);
        font.setColor(Color.BLACK);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 30);
        if (ClientGameManager.get().getWorld() != null)
            font.draw(batch, "Entities: " + ClientGameManager.get().getWorld().getEntities().size(), 0, 70);
        font.getData().setScale(1);
        font.setColor(Color.WHITE);
        batch.end();
    }

    public void tick() {
        if (currentScene != null)
            currentScene.tick();
    }

    public static SceneManager get() {
        return PvZMultiplayerClient.getInstance().getSceneManager();
    }

}
