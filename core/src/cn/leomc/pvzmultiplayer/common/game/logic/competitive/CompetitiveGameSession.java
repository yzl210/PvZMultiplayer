package cn.leomc.pvzmultiplayer.common.game.logic.competitive;

import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.logic.GameSession;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundCursorPositionPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundSunPacket;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class CompetitiveGameSession extends GameSession<CompetitiveGameSettings> {

    protected int zombiePoints = 0;


    public CompetitiveGameSession(CompetitiveGameSettings settings) {
        super(settings);
    }

    @Override
    public void tick() {
        super.tick();
        setZombiePoints(zombiePoints + 1);

        syncCursor(Team.PLANTS);
        syncCursor(Team.ZOMBIES);
    }

    private void syncCursor(Team team) {
        Map<ServerPlayer, Vector2> cursors = new HashMap<>(ServerManager.get().getPlayerList().getCursors());
        cursors.keySet().removeIf(player -> settings.getTeam(player.name()) != team);
        settings.getPlayers(team).forEach(player -> player.sendPacket(new ClientboundCursorPositionPacket(cursors)));
    }

    public int getZombiePoints() {
        return zombiePoints;
    }

    public void setZombiePoints(int zombiePoints) {
        this.zombiePoints = zombiePoints;
        syncZombiePoints();
    }

    public void syncZombiePoints() {
        settings.getPlayers(Team.ZOMBIES).forEach(player -> player.sendPacket(new ClientboundSunPacket(zombiePoints)));
    }

    @Override
    public void syncSun() {
        settings.getPlayers(Team.PLANTS).forEach(player -> player.sendPacket(new ClientboundSunPacket(sun)));
    }

    @Override
    public boolean canPlayerPlant(ServerPlayer player) {
        return settings.getTeam(player.name()) == Team.PLANTS;
    }


    public boolean spawnZombie(ServerPlayer player, ZombieType<?> zombie, int lane) {
        if (!settings.zombies.contains(zombie) || settings.getTeam(player.name()) != Team.ZOMBIES || !canSpawn(lane) || zombiePoints < zombie.sun() || getCooldown(zombie) > 0)
            return false;

        setZombiePoints(zombiePoints - zombie.sun());
        settings.zombies.forEach(this::addCooldown);
        world.spawnZombie(zombie, lane);
        return true;
    }

    public boolean canSpawn(int lane) {
        return lane >= 0 && lane < 5;
    }

}
