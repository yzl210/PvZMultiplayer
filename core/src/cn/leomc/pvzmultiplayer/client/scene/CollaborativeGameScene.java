package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.text.component.Component;

public class CollaborativeGameScene extends BaseScene {


    public CollaborativeGameScene() {
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
        return GameSettings.GameMode.COLLABORATIVE.getDisplayName();
    }

}
