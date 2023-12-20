package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.renderer.PlantGameRenderer;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.text.component.Component;

public class CollaborativeGameScene extends BaseScene {

    private PlantGameRenderer renderer;

    @Override
    public void create() {
        if (Sounds.MAIN_MENU.isPlaying())
            Sounds.MAIN_MENU.stop();

        ClientGameManager.get().getWorld().loadResources();

        renderer = new PlantGameRenderer(createStage());
        getInputMultiplexer().addProcessor(renderer);
    }


    @Override
    public void render() {
        renderer.render();
    }

    @Override
    public void tick() {
        ClientGameManager.get().getWorld().tick();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public PlantGameRenderer getRenderer() {
        return renderer;
    }


    @Override
    public Component getTitle() {
        return GameSettings.GameMode.COLLABORATIVE.getDisplayName();
    }
}
