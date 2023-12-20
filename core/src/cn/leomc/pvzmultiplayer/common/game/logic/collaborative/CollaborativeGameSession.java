package cn.leomc.pvzmultiplayer.common.game.logic.collaborative;

import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombies;
import cn.leomc.pvzmultiplayer.common.game.logic.GameSession;
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
            world.spawnZombie(Zombies.NORMAL, ThreadLocalRandom.current().nextInt(5));
    }

    @Override
    public boolean canPlayerPlant(ServerPlayer player) {
        return true;
    }
}
