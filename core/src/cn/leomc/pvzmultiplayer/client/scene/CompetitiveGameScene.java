package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.Team;
import cn.leomc.pvzmultiplayer.common.text.component.Component;

public class CompetitiveGameScene extends BaseScene {

    private final Team team;

    public CompetitiveGameScene(Team team) {
        this.team = team;
    }

    @Override
    public void create() {

    }

    @Override
    public void render() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void dispose() {

    }


    @Override
    public Component getTitle() {
        return GameSettings.GameMode.COMPETITIVE.getDisplayName();
    }

}
