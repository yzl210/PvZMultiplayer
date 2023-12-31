package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.renderer.CursorRenderer;
import cn.leomc.pvzmultiplayer.client.renderer.GameRenderer;
import cn.leomc.pvzmultiplayer.client.renderer.PlantGameRenderer;
import cn.leomc.pvzmultiplayer.client.renderer.ZombieGameRenderer;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.Team;
import cn.leomc.pvzmultiplayer.common.text.component.Component;

public class CompetitiveGameScene extends BaseScene {

    private final Team team;
    private GameRenderer renderer;
    private CursorRenderer cursorRenderer;

    public CompetitiveGameScene(Team team) {
        this.team = team;
    }

    @Override
    public void create() {
        if (Sounds.MAIN_MENU.isPlaying())
            Sounds.MAIN_MENU.stop();

        ClientGameManager.get().getWorld().loadResources();

        renderer = team == Team.PLANTS ? new PlantGameRenderer(createStage()) : new ZombieGameRenderer(createStage());
        getInputMultiplexer().addProcessor(renderer);

        cursorRenderer = new CursorRenderer();
    }

    @Override
    public void render() {
        renderer.render();
        cursorRenderer.render();
    }

    @Override
    public void tick() {
        ClientGameManager.get().getWorld().tick();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public GameRenderer getRenderer() {
        return renderer;
    }

    @Override
    public Component getTitle() {
        return GameSettings.GameMode.COMPETITIVE.getDisplayName();
    }

}
