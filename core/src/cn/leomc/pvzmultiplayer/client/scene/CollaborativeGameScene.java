package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Musics;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.world.Map;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundPlantPacket;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.Gdx;

public class CollaborativeGameScene extends BaseScene {

    private final PlantType<?> selectedPlant = Plants.PEASHOOTER;

    @Override
    public void create() {
        if (Musics.MENU.isPlaying())
            Musics.MENU.stop();
        ClientGameManager.get().getWorld().loadResources();
    }

    @Override
    public void render() {
        ClientGameManager.get().getWorld().render();
        if (selectedPlant != null)
            selectedPlant.texture().render(Gdx.input.getX() - 25, Gdx.graphics.getHeight() - Gdx.input.getY() - 25, 80, 80);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY;

        screenX -= (int) Map.DEFAULT.plantGridTopLeft().x;
        screenY -= (int) Map.DEFAULT.plantGridTopLeft().y;

        if (selectedPlant != null) {
            int x = (int) (screenX / Map.DEFAULT.plantGridDimension().x);
            int y = (int) (screenY / Map.DEFAULT.plantGridDimension().y);
            ClientGameManager.get().getConnection().sendPacket(new ServerboundPlantPacket(selectedPlant, x, y));
        }
        return true;
    }

    @Override
    public void tick() {
        ClientGameManager.get().getWorld().tick();
    }

    @Override
    public void dispose() {

    }


    @Override
    public Component getTitle() {
        return GameSettings.GameMode.COLLABORATIVE.getDisplayName();
    }

}
