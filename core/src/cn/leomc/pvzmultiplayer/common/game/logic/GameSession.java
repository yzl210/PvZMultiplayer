package cn.leomc.pvzmultiplayer.common.game.logic;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSession;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSession;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundPlantSeedsCooldownPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundSunPacket;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;

import java.util.HashMap;
import java.util.Map;

public class GameSession {

    private final GameSettings settings;

    private int sun;

    private final World world = new World(this);

    private final Map<PlantType<?>, Integer> plantSeedCooldowns = new HashMap<>();

    public GameSession(GameSettings settings) {
        this.settings = settings;
        setSun(5000);
    }

    public static GameSession create(GameSettings settings) {
        return switch (settings.mode()) {
            case COLLABORATIVE -> new CollaborativeGameSession(settings);
            case COMPETITIVE -> new CompetitiveGameSession(settings);
        };
    }

    public void tick() {
        plantSeedCooldowns.entrySet().removeIf(entry -> {
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

    public int getCooldown(PlantType<?> plant) {
        return plantSeedCooldowns.getOrDefault(plant, 0);
    }

    public void addCooldown(PlantType<?> plant) {
        plantSeedCooldowns.put(plant, plant.seedRechargeTicks());
        syncCooldowns();
    }

    public void removeCooldown(PlantType<?> plant) {
        plantSeedCooldowns.remove(plant);
        syncCooldowns();
    }

    public void syncCooldowns() {
        ServerManager.get().sendPacket(new ClientboundPlantSeedsCooldownPacket(plantSeedCooldowns));
    }

}
