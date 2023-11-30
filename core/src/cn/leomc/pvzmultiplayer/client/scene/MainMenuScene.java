package cn.leomc.pvzmultiplayer.client.scene;

import cn.leomc.pvzmultiplayer.client.logic.ClientGameManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScene extends BaseScene {

    private Stage stage;
    private Texture background;
    private TextField nameField;
    private Button hostButton;
    private Button joinButton;

    @Override
    public void create() {
        stage = createStage();

        background = new Texture("backgrounds/mainmenu.png");

        nameField = new TextField(ClientGameManager.get().getName(), getSkin());
        nameField.setPosition(stage.getWidth() / 2f - nameField.getWidth() / 2f, 350);
        nameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String text = nameField.getText();
                if (text.isBlank() || text.length() > 16)
                    event.cancel();
                ClientGameManager.get().setName(nameField.getText());
            }
        });
        stage.addActor(nameField);

        hostButton = new TextButton("Host", getSkin());
        hostButton.setPosition(stage.getWidth() / 2f - hostButton.getWidth() / 2f, 250);
        hostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                host();
            }
        });
        stage.addActor(hostButton);

        joinButton = new TextButton("Join", getSkin());
        joinButton.setPosition(stage.getWidth() / 2f - joinButton.getWidth() / 2f, 150);
        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                join();
            }
        });
        stage.addActor(joinButton);

    }

    @Override
    public void dispose() {
        getInputMultiplexer().removeProcessor(stage);
        background.dispose();
        stage.dispose();
    }

    @Override
    public void render() {
        SpriteBatch batch = getBatch();
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
        getFont().getData().setScale(0.2f);
        stage.draw();
        stage.act();
    }

    public void host() {
        ClientGameManager.get().host();
        setScene(new LobbyScene());
    }

    public void join() {
        setScene(new JoinServerScene());
    }

}
