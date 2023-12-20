package cn.leomc.pvzmultiplayer.common.game.logic;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSession;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSession;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSettings;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundEntityCooldownsPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundSunPacket;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public abstract class GameSession<S extends GameSettings> {

    protected final S settings;

    protected int sun;

    protected final World world = new World(this);

    private final Map<EntityType<?, ?>, Integer> entityCooldowns = new HashMap<>();

    public GameSession(S settings) {
        this.settings = settings;
        setSun(5000);
    }

    public static GameSession<?> create(GameSettings settings) {
        return switch (settings.mode()) {
            case COLLABORATIVE -> new CollaborativeGameSession((CollaborativeGameSettings) settings);
            case COMPETITIVE -> new CompetitiveGameSession((CompetitiveGameSettings) settings);
        };
    }

    public void tick() {
        entityCooldowns.entrySet().removeIf(entry -> {
            entry.setValue(entry.getValue() - 1);
            return entry.getValue() <= 0;
        });
        syncCooldowns();
        world.tick();
    }

    public World getWorld() {
        return world;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
        syncSun();
    }

    public void syncSun() {
        ServerManager.get().sendPacket(new ClientboundSunPacket(sun));
    }

    public int getCooldown(EntityType<?, ?> plant) {
        return entityCooldowns.getOrDefault(plant, 0);
    }

    public void addCooldown(EntityType<?, ?> entity) {
        if (entity instanceof PlantType<?> plant)
            entityCooldowns.put(plant, plant.seedRechargeTicks());
        if (entity instanceof ZombieType<?> zombie)
            entityCooldowns.put(zombie, zombie.eggRechargeTicks());
        syncCooldowns();
    }

    public void removeCooldown(EntityType<?, ?> plant) {
        entityCooldowns.remove(plant);
        syncCooldowns();
    }

    public void syncCooldowns() {
        ServerManager.get().sendPacket(new ClientboundEntityCooldownsPacket(entityCooldowns));
    }

    public boolean plant(ServerPlayer player, int x, int y, PlantType<?> type) {
        if (!world.canPlant(x, y) || getSun() < type.sun() || getCooldown(type) > 0 || !canPlayerPlant(player))
            return false;

        addCooldown(type);
        setSun(getSun() - type.sun());

        Vector2 position = new Vector2(x * world.getMap().plantGridDimension().x + world.getMap().plantGridTopLeft().x, y * world.getMap().plantGridDimension().y + world.getMap().plantGridTopLeft().y);


        Plant plant = type.create(EntityCreationContext.of(position, world));

        world.setPlant(x, y, plant);
        return true;
    }


    public abstract boolean canPlayerPlant(ServerPlayer player);

}
