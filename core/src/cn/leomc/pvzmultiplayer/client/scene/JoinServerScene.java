package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.common.text.component.Component;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class JoinServerScene extends BaseScene {

    private Stage stage;
    private Texture background;
    private Label addressLabel;
    private TextField addressField;
    private Button connectButton;
    private Label errorLabel;


    @Override
    public void create() {
        stage = createStage();
        background = new Texture("backgrounds/mainmenu.png");

        addressLabel = new Label("Server Address:", getSkin());
        addressLabel.setColor(Color.BLACK);
        addressLabel.setPosition(stage.getWidth() / 2f - addressLabel.getWidth() / 2f, 305);
        stage.addActor(addressLabel);

        addressField = new TextField("", getSkin());
        addressField.setPosition(stage.getWidth() / 2f - addressField.getWidth() / 2f, 275);
        stage.addActor(addressField);

        connectButton = new TextButton("Connect", getSkin());
        connectButton.setPosition(stage.getWidth() / 2f - connectButton.getWidth() / 2f, 150);
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                connect();
            }
        });
        stage.addActor(connectButton);


        errorLabel = new Label("", getSkin());
        errorLabel.setFontScale(1.5f);
        stage.addActor(errorLabel);
    }

    @Override
    public void render() {
        SpriteBatch batch = getBatch();
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        getInputMultiplexer().removeProcessor(stage);
        stage.dispose();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("Join Server");
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            setScene(new MainMenuScene());
            return true;
        }
        return super.keyDown(keycode);
    }

    private void connect() {
        if (addressField.getText().isBlank()) {
            setErrorMessage(Component.translatable("Please enter a server address"));
            return;
        }

        setMessage(Component.translatable("Connecting..."));

        runLater(() -> {
            try {
                ClientGameManager.get().connect(addressField.getText());
            } catch (Exception e) {
                e.printStackTrace();
                PvZMultiplayerClient.getInstance()
                        .runLater(() -> setErrorMessage(Component.translatable("Failed to connect to server: " + e.getMessage())));
            }
        });
    }

    public void setMessage(Component message) {
        errorLabel.setText(message.get());
        errorLabel.setSize(errorLabel.getPrefWidth(), errorLabel.getPrefHeight());
        errorLabel.setPosition(stage.getWidth() / 2f - errorLabel.getWidth() / 2f, 100);
        errorLabel.setColor(Color.BLACK);
    }

    public void setErrorMessage(Component message) {
        setMessage(message);
        errorLabel.setColor(Color.RED);
    }

}
