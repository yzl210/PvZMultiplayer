package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.client.widget.Bar;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.world.Interactable;
import cn.leomc.pvzmultiplayer.common.game.content.world.Map;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundPlantPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ServerboundEntityInteractPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ServerboundShovelPacket;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CollaborativeGameScene extends BaseScene {

    private Bar bar;
    private PlantType<?> selectedPlant;
    private Renderable shovel;
    private boolean selectedShovel;
    private Stage stage;

    @Override
    public void create() {
        if (Sounds.MAIN_MENU.isPlaying())
            Sounds.MAIN_MENU.stop();

        ClientGameManager.get().getWorld().loadResources();

        stage = createStage();
        bar = new Bar(new Bar.BarCallback() {
            @Override
            public int sun() {
                return ClientGameManager.get().getSun();
            }

            @Override
            public void selectPlant(PlantType<?> plant) {
                CollaborativeGameScene.this.selectPlant(plant);
            }

            @Override
            public PlantType<?> selectedPlant() {
                return selectedPlant;
            }

            @Override
            public boolean shovelSelected() {
                return selectedShovel;
            }

            @Override
            public void selectShovel(boolean shovel) {
                selectedPlant = null;
                selectedShovel = shovel;
            }
        }, getSkin());
        bar.addPlant(Plants.SUNFLOWER);
        bar.addPlant(Plants.PEASHOOTER);
        bar.setScale(0.9f);
        bar.setPosition(0, Gdx.graphics.getHeight() - bar.getHeight() * 0.9f);

        stage.addActor(bar);

        shovel = new FixedTexture("textures/shovel.png");
    }


    @Override
    public void render() {
        ClientGameManager.get().getWorld().render();
        stage.act();
        stage.draw();

        Vector2 mouse = PvZMultiplayerClient.getInstance().getViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (selectedShovel)
            shovel.render(mouse.x - 90, mouse.y - 25, 112, 32);
        if (selectedPlant != null) {
            Vector2 gridPos = getGridPosition(mouse);
            if (ClientGameManager.get().getWorld().canPlant((int) gridPos.x, (int) gridPos.y)) {
                Map map = ClientGameManager.get().getWorld().getMap();
                selectedPlant.texture().render(gridPos.x * map.plantGridDimension().x + map.plantGridTopLeft().x,
                        gridPos.y * map.plantGridDimension().y + map.plantGridTopLeft().y,
                        selectedPlant.dimension().x,
                        selectedPlant.dimension().y,
                        new Color(1, 1, 1, 0.5f));
            }

            selectedPlant.texture().render(mouse.x - 25, mouse.y - 25, selectedPlant.dimension().x, selectedPlant.dimension().y);
        }
    }

    @Override
    public boolean touchDown(int a, int b, int pointer, int button) {
        Viewport viewport = PvZMultiplayerClient.getInstance().getViewport();
        Vector2 mouse = viewport.unproject(new Vector2(a, b));

        int screenX = (int) mouse.x;
        int screenY = (int) mouse.y;

        Vector2 gridPos = getGridPosition(mouse);

        if (selectedShovel) {
            ClientGameManager.get().getConnection().sendPacket(new ServerboundShovelPacket((int) gridPos.x, (int) gridPos.y));
        }

        if (selectedPlant != null) {
            if (gridPos.x < 0 || gridPos.y < 0 || gridPos.x > 8 || gridPos.y > 4)
                return false;
            ClientGameManager.get().getConnection().sendPacket(new ServerboundPlantPacket(selectedPlant, (int) gridPos.x, (int) gridPos.y));
            return true;
        }

        ClientGameManager.get().getWorld().getEntities().forEach(entity -> {
            if (entity instanceof Interactable) {
                Vector2 position = entity.position();
                Vector2 dimension = entity.type().dimension();
                if (screenX >= position.x && screenX <= position.x + dimension.x && screenY >= position.y && screenY <= position.y + dimension.y)
                    ClientGameManager.get().getConnection().sendPacket(new ServerboundEntityInteractPacket(entity.id(), screenX, screenY, pointer, button));
            }
        });

        return false;
    }

    public Vector2 getGridPosition(Vector2 mouse) {
        int gridX = (int) (mouse.x - Map.DEFAULT.plantGridTopLeft().x);
        int gridY = (int) (mouse.y - Map.DEFAULT.plantGridTopLeft().y);

        int x = (int) (gridX / Map.DEFAULT.plantGridDimension().x);
        int y = (int) (gridY / Map.DEFAULT.plantGridDimension().y);
        return new Vector2(x, y);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            selectedShovel = false;
            selectedPlant = null;
        } else if (keycode == Input.Keys.S)
            selectShovel();
        else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int i = keycode - Input.Keys.NUM_1;
            if (i < bar.getPlantSeeds().size())
                selectPlant(bar.getPlantSeeds().get(i).getPlant());
        }

        return false;
    }

    @Override
    public void tick() {
        ClientGameManager.get().getWorld().tick();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void selectShovel() {
        selectedShovel = !selectedShovel;
        selectedPlant = null;
    }

    public void selectPlant(PlantType<?> plant) {
        if ((plant != null && ClientGameManager.get().getSun() < plant.sun()) || plant == selectedPlant || ClientGameManager.get().getPlantSeedCooldown(plant) > 0)
            return;
        selectedPlant = plant;
        selectedShovel = false;
        Sounds.SEED_SELECT.play();
    }

    public void plantResult(boolean success) {
        if (success)
            selectedPlant = null;
    }

    public void shovelResult(boolean success) {
        if (success)
            selectedShovel = false;
    }

    @Override
    public Component getTitle() {
        return GameSettings.GameMode.COLLABORATIVE.getDisplayName();
    }


}
