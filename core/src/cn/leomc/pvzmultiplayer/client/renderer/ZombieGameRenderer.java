package cn.leomc.pvzmultiplayer.client.renderer;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombies;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ServerboundSpawnZombiePacket;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ZombieGameRenderer extends GameRenderer {

    public ZombieGameRenderer(Stage stage) {
        super(stage);

        bar.addEntity(Zombies.NORMAL);
    }

    @Override
    public void render() {
        super.render();
        if (selectedEntity != null) {
            Vector2 gridPos = getGridPosition(getMousePosition());
            if (gridPos.y >= 0 && gridPos.y < 5)
                selectedEntity.texture().render(Constants.WIDTH - 100,
                        gridPos.y * 100 + 50,
                        selectedEntity.dimension().x,
                        selectedEntity.dimension().y,
                        new Color(1, 1, 1, 0.5f));
        }
    }

    @Override
    protected void addEntity(Vector2 gridPos, EntityType<?, ?> entity) {
        if (gridPos.y < 0 || gridPos.y > 4)
            return;
        ClientGameManager.get().getConnection().sendPacket(new ServerboundSpawnZombiePacket((ZombieType<?>) entity, (int) gridPos.y));
    }

    @Override
    public int getPoints() {
        return ClientGameManager.get().getSun();
    }

    @Override
    public int getCost(EntityType<?, ?> entity) {
        return ((ZombieType<?>) entity).sun();
    }

    @Override
    public int getCooldown(EntityType<?, ?> entity) {
        return ((ZombieType<?>) entity).eggRechargeTicks();
    }
}
