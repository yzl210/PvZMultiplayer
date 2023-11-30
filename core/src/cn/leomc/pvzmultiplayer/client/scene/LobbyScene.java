package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class LobbyScene extends BaseScene {

    private Stage stage;
    private Drawable background;
    private List<String> playerList;
    private ButtonGroup<TextButton> gameModeGroup;

    @Override
    public void create() {
        stage = createStage();

        background = getSkin().get(List.ListStyle.class).background;

        TextButton leaveButton = new TextButton("Leave", getSkin());
        leaveButton.setPosition(0, 0);
        leaveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runLater(() -> ClientGameManager.get().disconnect());
            }
        });
        stage.addActor(leaveButton);

        VerticalGroup leftGroup = new VerticalGroup();
        leftGroup.setPosition(0, 0);
        leftGroup.setSize(Constants.WIDTH / 2f, stage.getHeight());
        leftGroup.align(Align.center);
        stage.addActor(leftGroup);

        Label playersLabel = new Label("Players:", getSkin());
        playersLabel.setFontScale(2f);
        leftGroup.addActor(playersLabel);

        playerList = new List<>(getSkin());

        playerList.setPosition(0, 0);
        playerList.setSize(Constants.WIDTH / 2f, stage.getHeight());
        refreshPlayerList();
        leftGroup.addActor(playerList);

        VerticalGroup rightGroup = new VerticalGroup();
        rightGroup.setPosition(Constants.WIDTH / 2f, 0);
        rightGroup.setSize(Constants.WIDTH / 2f, stage.getHeight());
        rightGroup.align(Align.center);
        stage.addActor(rightGroup);

        Label gameSettings = new Label("Game Settings:", getSkin());
        gameSettings.setFontScale(2);
        rightGroup.addActor(gameSettings);

        Label gameModeLabel = new Label("Game Mode:", getSkin());
        gameModeLabel.setFontScale(1.5f);
        rightGroup.addActor(gameModeLabel);

        gameModeGroup = new ButtonGroup<>();
        gameModeGroup.setMinCheckCount(1);
        gameModeGroup.setMaxCheckCount(1);
        gameModeGroup.setUncheckLast(true);

        for (GameSettings.GameMode mode : GameSettings.GameMode.values()) {
            CheckBox button = new CheckBox(mode.name(), getSkin());
            gameModeGroup.add(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    ClientGameManager.get().getGameSettings().mode = GameSettings.GameMode.valueOf(button.getText().toString());
                    ClientGameManager.get().sendGameSettings();
                }
            });
            rightGroup.addActor(button);
        }
        gameModeGroup.setChecked(GameSettings.GameMode.COLLABORATIVE.name());

        TextButton startButton = new TextButton("Start", getSkin());
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //
            }
        });

        rightGroup.addActor(startButton);


    }

    @Override
    public void render() {
        SpriteBatch batch = getBatch();
        batch.begin();
        background.draw(batch, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();
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

    public void refreshSettings() {
        GameSettings settings = ClientGameManager.get().getGameSettings();
        gameModeGroup.setChecked(settings.mode.name());
    }
}
