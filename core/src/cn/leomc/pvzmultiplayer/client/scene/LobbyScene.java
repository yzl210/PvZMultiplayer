package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.client.renderer.CursorRenderer;
import cn.leomc.pvzmultiplayer.client.widget.HorizontalLabeledTextField;
import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.CompetitiveGameSettings;
import cn.leomc.pvzmultiplayer.common.game.logic.competitive.Team;
import cn.leomc.pvzmultiplayer.common.networking.packet.ServerboundStartGamePacket;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.function.Consumer;

public class LobbyScene extends BaseScene {

    private Stage stage;
    private Drawable background;
    private HorizontalGroup leftGroup;
    private ButtonGroup<TextButton> gameModeGroup;
    private TextButton startButton;

    private final ArrayList<Consumer<GameSettings>> settingsListeners = new ArrayList<>();

    private CursorRenderer cursorRenderer;

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

        leftGroup = new HorizontalGroup();
        leftGroup.setPosition(0, 0);
        leftGroup.setSize(Constants.WIDTH / 2f, stage.getHeight());
        leftGroup.align(Align.center);
        stage.addActor(leftGroup);

        VerticalGroup rightGroup = new VerticalGroup();
        rightGroup.setPosition(Constants.WIDTH / 2f, 0);
        rightGroup.setSize(Constants.WIDTH / 2f, stage.getHeight());
        rightGroup.align(Align.center);
        stage.addActor(rightGroup);
        Label gameModeLabel = new Label("Game Mode:", getSkin());
        gameModeLabel.setFontScale(2);
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
                    if (!button.isChecked())
                        return;

                    runLater(() -> {
                        ClientGameManager.get().setGameSettings(mode.createSettings());
                        ClientGameManager.get().sendGameSettings();
                    });
                }
            });
            rightGroup.addActor(button);
        }
        gameModeGroup.setChecked(ClientGameManager.get().getGameSettings().mode().name());

        Label gameSettingsLabel = new Label("Game Settings:", getSkin());
        gameSettingsLabel.setFontScale(2);
        rightGroup.addActor(gameSettingsLabel);

        HorizontalLabeledTextField initialSun = new HorizontalLabeledTextField("Initial Sun: ", "", getSkin());
        settingsListeners.add(settings -> initialSun.getTextField().setText(String.valueOf(settings.initialSun)));
        initialSun.getTextField().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    int sun = Integer.parseInt(initialSun.getText());
                    runLater(() -> {
                        ClientGameManager.get().getGameSettings().initialSun = sun;
                        ClientGameManager.get().sendGameSettings();
                    });
                } catch (NumberFormatException e) {
                    event.cancel();
                }
            }
        });
        rightGroup.addActor(initialSun);

        HorizontalLabeledTextField sunAmount = new HorizontalLabeledTextField("Sun Amount: ", "", getSkin());
        settingsListeners.add(settings -> sunAmount.getTextField().setText(String.valueOf(settings.sunAmount)));
        sunAmount.getTextField().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    int amount = Integer.parseInt(sunAmount.getText());
                    runLater(() -> {
                        ClientGameManager.get().getGameSettings().sunAmount = amount;
                        ClientGameManager.get().sendGameSettings();
                    });
                } catch (NumberFormatException e) {
                    event.cancel();
                }
            }
        });
        rightGroup.addActor(sunAmount);

        CheckBox lazyModeButton = new CheckBox("Lazy Mode", getSkin());
        settingsListeners.add(settings -> lazyModeButton.setChecked(settings.lazyMode));
        lazyModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runLater(() -> {
                    ClientGameManager.get().getGameSettings().lazyMode = lazyModeButton.isChecked();
                    ClientGameManager.get().sendGameSettings();
                });
            }
        });
        rightGroup.addActor(lazyModeButton);


        startButton = new TextButton("Start", getSkin());
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runLater(() -> ClientGameManager.get().getConnection().sendPacket(new ServerboundStartGamePacket()));
            }
        });
        startButton.setDisabled(true);


        rightGroup.addActor(startButton);

        settingsListeners.forEach(listener -> listener.accept(ClientGameManager.get().getGameSettings()));

        cursorRenderer = new CursorRenderer();
    }

    private void collaboration(Group leftGroup) {
        VerticalGroup group = new VerticalGroup();
        leftGroup.addActor(group);

        Label playersLabel = new Label("Players:", getSkin());
        playersLabel.setFontScale(2f);
        group.addActor(playersLabel);

        List<String> playerList = new List<>(getSkin());
        playerList.setItems(ClientGameManager.get().getPlayerList().toArray(String[]::new));

        playerList.setPosition(0, 0);
        playerList.setSize(Constants.WIDTH / 2f, stage.getHeight());
        group.addActor(playerList);
    }

    private void competitive(Group leftGroup) {
        CompetitiveGameSettings gameSettings = (CompetitiveGameSettings) ClientGameManager.get().getGameSettings();
        VerticalGroup plantsGroup = new VerticalGroup();
        leftGroup.addActor(plantsGroup);

        Label plantsLabel = new Label("Team Plants:", getSkin());
        plantsLabel.setFontScale(1.5f);
        plantsGroup.addActor(plantsLabel);

        List<String> plantsTeamPlayerList = new List<>(getSkin());
        plantsTeamPlayerList.setItems(gameSettings.getPlayersNames(Team.PLANTS).toArray(String[]::new));
        plantsGroup.addActor(plantsTeamPlayerList);

        TextButton joinPlantsButton = new TextButton("Join", getSkin());
        joinPlantsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runLater(() -> {
                    if (ClientGameManager.get().getGameSettings() instanceof CompetitiveGameSettings settings) {
                        settings.setPlayerTeam(ClientGameManager.get().getName(), Team.PLANTS);
                        ClientGameManager.get().sendGameSettings();
                    }
                });
            }
        });
        plantsGroup.addActor(joinPlantsButton);

        Label separatorLabel = new Label(" Versus ", getSkin());
        separatorLabel.setFontScale(1.5f);
        leftGroup.addActor(separatorLabel);

        VerticalGroup zombiesGroup = new VerticalGroup();
        leftGroup.addActor(zombiesGroup);

        Label zombiesLabel = new Label("Team Zombies:", getSkin());
        zombiesLabel.setFontScale(1.5f);
        zombiesGroup.addActor(zombiesLabel);

        List<String> zombiesTeamPlayerList = new List<>(getSkin());
        zombiesTeamPlayerList.setItems(gameSettings.getPlayersNames(Team.ZOMBIES).toArray(String[]::new));
        zombiesGroup.addActor(zombiesTeamPlayerList);

        TextButton joinZombiesButton = new TextButton("Join", getSkin());
        joinZombiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runLater(() -> {
                    if (ClientGameManager.get().getGameSettings() instanceof CompetitiveGameSettings settings) {
                        settings.setPlayerTeam(ClientGameManager.get().getName(), Team.ZOMBIES);
                        ClientGameManager.get().sendGameSettings();
                    }
                });
            }
        });
        zombiesGroup.addActor(joinZombiesButton);
    }


    @Override
    public void render() {
        SpriteBatch batch = getBatch();
        batch.begin();
        background.draw(batch, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();
        stage.draw();
        stage.act();
        cursorRenderer.render();
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
        GameSettings settings = ClientGameManager.get().getGameSettings();
        return Component.translatable("Lobby" + (settings == null ? "" : " - " + settings.mode().getDisplayName().get()));
    }

    public void refresh() {
        leftGroup.clear();
        GameSettings settings = ClientGameManager.get().getGameSettings();
        gameModeGroup.setChecked(settings.mode().name());
        settingsListeners.forEach(listener -> listener.accept(settings));
        switch (settings.mode()) {
            case COLLABORATIVE -> collaboration(leftGroup);
            case COMPETITIVE -> competitive(leftGroup);
        }
        startButton.setDisabled(!settings.canStart());
    }
}
