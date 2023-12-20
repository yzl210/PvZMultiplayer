package cn.leomc.pvzmultiplayer.common.game.logic.collaborative;

import cn.leomc.pvzmultiplayer.common.game.logic.GameSession;
import cn.leomc.pvzmultiplayer.common.networking.packet.ClientboundCursorPositionPacket;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;

import java.util.concurrent.ThreadLocalRandom;

public class CollaborativeGameSession extends GameSession<CollaborativeGameSettings> {

    public CollaborativeGameSession(CollaborativeGameSettings settings) {
        super(settings);
    }

    int timer;

    @Override
    public void tick() {
        super.tick();
        timer++;
        if (timer % 100 == 0)
            world.spawnZombie(settings.zombies.get(ThreadLocalRandom.current().nextInt(settings.zombies.size())), ThreadLocalRandom.current().nextInt(5));

        ServerManager.get().sendPacket(new ClientboundCursorPositionPacket(ServerManager.get().getPlayerList().getCursors()));
    }

    @Override
    public boolean canPlayerPlant(ServerPlayer player) {
        return true;
    }
}
