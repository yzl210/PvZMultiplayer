package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Musics;
import cn.leomc.pvzmultiplayer.client.widget.SunCard;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.world.Interactable;
import cn.leomc.pvzmultiplayer.common.game.content.world.Map;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundPlantPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ServerboundEntityInteractPacket;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CollaborativeGameScene extends BaseScene {

    private PlantType<?> selectedPlant = Plants.SUNFLOWER;
    private Stage stage;

    @Override
    public void create() {
        if (Musics.MENU.isPlaying())
            Musics.MENU.stop();

        ClientGameManager.get().getWorld().loadResources();

        stage = createStage();
        SunCard sunCard = new SunCard(() -> ClientGameManager.get().getSun(), getSkin());
        sunCard.setScale(1.25f);
        sunCard.setPosition(0, Gdx.graphics.getHeight() - sunCard.getHeight());
        stage.addActor(sunCard);
    }


    @Override
    public void render() {
        ClientGameManager.get().getWorld().render();
        stage.act();
        stage.draw();
        if (selectedPlant != null)
            selectedPlant.texture().render(Gdx.input.getX() - 25, Gdx.graphics.getHeight() - Gdx.input.getY() - 25, selectedPlant.dimension().x, selectedPlant.dimension().y);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int realY = Gdx.graphics.getHeight() - screenY;
        int gridY = realY - (int) Map.DEFAULT.plantGridTopLeft().y;

        int gridX = screenX - (int) Map.DEFAULT.plantGridTopLeft().x;

        if (selectedPlant != null) {
            int x = (int) (gridX / Map.DEFAULT.plantGridDimension().x);
            int y = (int) (gridY / Map.DEFAULT.plantGridDimension().y);
            ClientGameManager.get().getConnection().sendPacket(new ServerboundPlantPacket(selectedPlant, x, y));
        }

        ClientGameManager.get().getWorld().getEntities().forEach(entity -> {
            if (entity instanceof Interactable) {
                Vector2 position = entity.position();
                Vector2 dimension = entity.type().dimension();
                if (screenX >= position.x && screenX <= position.x + dimension.x && realY >= position.y && realY <= position.y + dimension.y)
                    ClientGameManager.get().getConnection().sendPacket(new ServerboundEntityInteractPacket(entity.id(), screenX, realY, pointer, button));
            }
        });

        return true;
    }

    @Override
    public void tick() {
        ClientGameManager.get().getWorld().tick();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void plantResult(boolean success) {
        if (success)
            selectedPlant = selectedPlant == Plants.SUNFLOWER ? Plants.PEASHOOTER : Plants.SUNFLOWER;
    }

    @Override
    public Component getTitle() {
        return GameSettings.GameMode.COLLABORATIVE.getDisplayName();
    }

}
