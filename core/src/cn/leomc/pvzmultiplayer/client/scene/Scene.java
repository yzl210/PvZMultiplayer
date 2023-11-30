package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Scene extends InputProcessor {
    Component getTitle();

    void create();

    void render();

    void tick();

    void dispose();


    default Stage createStage() {
        Stage stage = new Stage(getViewport(), getBatch());
        getInputMultiplexer().addProcessor(stage);
        return stage;
    }

    default Skin getSkin() {
        return PvZMultiplayerClient.getInstance().getSkin();
    }

    default SpriteBatch getBatch() {
        return PvZMultiplayerClient.getInstance().getBatch();
    }

    default BitmapFont getFont() {
        return PvZMultiplayerClient.getInstance().getFont();
    }

    default InputMultiplexer getInputMultiplexer() {
        return PvZMultiplayerClient.getInstance().getInputMultiplexer();
    }

    default Viewport getViewport() {
        return PvZMultiplayerClient.getInstance().getViewport();
    }

    default void setScene(Scene scene) {
        PvZMultiplayerClient.getInstance().getSceneManager().setScene(scene);
    }

}
