package cn.leomc.pvzmultiplayer.common.game.logic;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSession;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSession;

public class GameSession {

    private final GameSettings settings;

    private int sun = 100;

    private final World world = new World(this);


    public GameSession(GameSettings settings) {
        this.settings = settings;
    }

    public static GameSession create(GameSettings settings) {
        return switch (settings.mode()) {
            case COLLABORATIVE -> new CollaborativeGameSession(settings);
            case COMPETITIVE -> new CompetitiveGameSession(settings);
        };
    }

    public void tick() {
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
    }

}
