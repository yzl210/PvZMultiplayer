package cn.leomc.pvzmultiplayer.client;

import cn.leomc.pvzmultiplayer.client.logic.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.scene.MainMenuScene;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PvZMultiplayerClient extends ApplicationAdapter {

    private static PvZMultiplayerClient INSTANCE;

    private SpriteBatch batch;
    private Skin skin;
    private BitmapFont font;
    private InputMultiplexer inputMultiplexer;
    private Viewport viewport;

    private final ClientGameManager gameManager = new ClientGameManager();

    private final SceneManager sceneManager = new SceneManager();

    public PvZMultiplayerClient() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skins/freezing/freezing-ui.json"));
        font = generateFont();
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        sceneManager.setScene(new MainMenuScene());
    }

    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/calibri.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        return generator.generateFont(parameter);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        sceneManager.render();
        sceneManager.tick();
        Gdx.graphics.setTitle(sceneManager.getTitle());
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Skin getSkin() {
        return skin;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public ClientGameManager getGameManager() {
        return gameManager;
    }

    public static PvZMultiplayerClient getInstance() {
        return INSTANCE;
    }
}
