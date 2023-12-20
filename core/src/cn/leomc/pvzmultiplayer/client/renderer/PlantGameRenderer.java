package cn.leomc.pvzmultiplayer.client.renderer;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.world.Map;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundPlantPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ServerboundShovelPacket;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class PlantGameRenderer extends GameRenderer {

    private Renderable shovel;
    private boolean shovelSelected;


    public PlantGameRenderer(Stage stage) {
        super(stage);
        ClientGameManager.get().getGameSettings().plants.forEach(bar::addEntity);
        shovel = new FixedTexture("textures/shovel.png");
    }

    public void render() {
        super.render();
        Vector2 mouse = getMousePosition();

        if (shovelSelected) {
            shovel.render(mouse.x - 90, mouse.y - 25, 112, 32);
        } else if (selectedEntity != null) {
            Vector2 gridPos = getGridPosition(getMousePosition());
            if (ClientGameManager.get().getWorld().canPlant((int) gridPos.x, (int) gridPos.y)) {
                Map map = ClientGameManager.get().getWorld().getMap();
                selectedEntity.texture().render(gridPos.x * map.plantGridDimension().x + map.plantGridTopLeft().x,
                        gridPos.y * map.plantGridDimension().y + map.plantGridTopLeft().y,
                        selectedEntity.dimension().x,
                        selectedEntity.dimension().y,
                        new Color(1, 1, 1, 0.5f));
            }
        }
    }


    public void dispose() {
        super.dispose();
    }


    @Override
    public boolean touchDown(int a, int b, int pointer, int button) {
        if (shovelSelected) {
            Vector2 gridPos = getGridPosition(getMousePosition());
            ClientGameManager.get().getConnection().sendPacket(new ServerboundShovelPacket((int) gridPos.x, (int) gridPos.y));
        }

        return super.touchDown(a, b, pointer, button);
    }

    @Override
    protected void addEntity(Vector2 gridPos, EntityType<?, ?> plant) {
        if (gridPos.x < 0 || gridPos.y < 0 || gridPos.x > 8 || gridPos.y > 4)
            return;
        ClientGameManager.get().getConnection().sendPacket(new ServerboundPlantPacket((PlantType<?>) plant, (int) gridPos.x, (int) gridPos.y));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
            shovelSelected = false;
        else if (keycode == Input.Keys.S)
            selectShovel(!shovelSelected);

        return super.keyDown(keycode);
    }

    @Override
    public void selectEntity(EntityType<?, ?> entity) {
        super.selectEntity(entity);
        shovelSelected = false;
    }

    @Override
    public int getPoints() {
        return ClientGameManager.get().getSun();
    }

    @Override
    public int getCost(EntityType<?, ?> entity) {
        return ((PlantType<?>) entity).sun();
    }

    @Override
    public int getCooldown(EntityType<?, ?> entity) {
        return ((PlantType<?>) entity).seedRechargeTicks();
    }

    public boolean isShovelSelected() {
        return shovelSelected;
    }

    public void selectShovel(boolean selected) {
        shovelSelected = selected;
        selectedEntity = null;
        Sounds.SHOVEL.play();
    }


    public void shovelResult(boolean success) {
        if (success) {
            shovelSelected = false;
            Sounds.PLANT.play();
        }
    }

}
