package cn.leomc.pvzmultiplayer.client.renderer;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.widget.Bar;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.world.Interactable;
import cn.leomc.pvzmultiplayer.common.game.content.world.Map;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ServerboundEntityInteractPacket;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameRenderer extends InputAdapter {

    protected final Stage stage;
    protected EntityType<?, ?> selectedEntity;
    protected Bar bar;


    public GameRenderer(Stage stage) {
        this.stage = stage;

        bar = new Bar(this, PvZMultiplayerClient.getInstance().getSkin());
        bar.setScale(0.9f);
        bar.setPosition(0, Gdx.graphics.getHeight() - bar.getHeight() * 0.9f);
        stage.addActor(bar);
    }

    public void render() {
        ClientGameManager.get().getWorld().render();
        stage.act();
        stage.draw();

        Vector2 mouse = getMousePosition();

        if (selectedEntity != null)
            selectedEntity.texture().render(mouse.x - 25, mouse.y - 25, selectedEntity.dimension().x, selectedEntity.dimension().y);
    }

    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean touchDown(int a, int b, int pointer, int button) {
        Viewport viewport = PvZMultiplayerClient.getInstance().getViewport();
        Vector2 mouse = viewport.unproject(new Vector2(a, b));

        int screenX = (int) mouse.x;
        int screenY = (int) mouse.y;

        Vector2 gridPos = getGridPosition(mouse);

        if (selectedEntity != null) {
            addEntity(gridPos, selectedEntity);
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

    protected abstract void addEntity(Vector2 gridPos, EntityType<?, ?> plant);

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
            selectedEntity = null;
        else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int i = keycode - Input.Keys.NUM_1;
            if (i < bar.getEntityCards().size())
                selectEntity(bar.getEntityCards().get(i).getEntity());
        }

        return false;
    }

    public void selectEntity(EntityType<?, ?> entity) {
        if (entity == selectedEntity || (entity != null && ClientGameManager.get().getSun() < getCost(entity)) || ClientGameManager.get().getEntityCooldown(entity) > 0)
            return;
        selectedEntity = entity;
        Sounds.SEED_SELECT.play();
    }

    public void addEntityResult(boolean success) {
        if (success) {
            selectedEntity = null;
            Sounds.PLANT.play();
        }
    }

    public abstract int getPoints();

    public abstract int getCost(EntityType<?, ?> entity);

    public abstract int getCooldown(EntityType<?, ?> entity);

    public Vector2 getMousePosition() {
        return PvZMultiplayerClient.getInstance().getViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
    }

    public Vector2 getGridPosition(Vector2 mouse) {
        int gridX = (int) (mouse.x - Map.DEFAULT.plantGridTopLeft().x);
        int gridY = (int) (mouse.y - Map.DEFAULT.plantGridTopLeft().y);

        int x = (int) (gridX / Map.DEFAULT.plantGridDimension().x);
        int y = (int) (gridY / Map.DEFAULT.plantGridDimension().y);
        return new Vector2(x, y);
    }

    public EntityType<?, ?> getSelectedEntity() {
        return selectedEntity;
    }
}
