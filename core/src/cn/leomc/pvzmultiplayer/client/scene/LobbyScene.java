package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;

public class LobbyScene extends BaseScene {

    private Stage stage;
    private List<String> playerList;

    @Override
    public void create() {
        stage = createStage();

        playerList = new List<>(getSkin());
        playerList.setPosition(0, 0);
        playerList.setSize(Constants.WIDTH / 2f, stage.getHeight());
        refreshPlayerList();
        stage.addActor(playerList);
    }

    @Override
    public void render() {
        stage.draw();
        stage.act();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("Lobby");
    }

    public void refreshPlayerList() {
        if (ClientGameManager.get().getPlayerList() == null)
            return;
        playerList.setItems(ClientGameManager.get().getPlayerList().toArray(String[]::new));
    }

}
