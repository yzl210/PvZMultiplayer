package cn.leomc.pvzmultiplayer.common.game.logic;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.collaborative.CollaborativeGameSession;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSession;

public class GameSession {

    private final GameSettings settings;



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

    }
}
