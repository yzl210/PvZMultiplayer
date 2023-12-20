package cn.leomc.pvzmultiplayer.client;

import cn.leomc.pvzmultiplayer.client.scene.MainMenuScene;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PvZMultiplayerClient extends ApplicationAdapter {
    public static ApplicationLogger LOGGER;

    private static Thread renderThread;
    private static PvZMultiplayerClient INSTANCE;

    private SpriteBatch batch;
    private ShapeDrawer shapeDrawer;

    private Skin skin;
    private BitmapFont font;
    private InputMultiplexer inputMultiplexer;
    private Viewport viewport;

    private final ClientGameManager gameManager = new ClientGameManager();
    private final SceneManager sceneManager = new SceneManager();

    private final Queue<Runnable> tasksQueue = new ConcurrentLinkedQueue<>();

    public PvZMultiplayerClient() {
        renderThread = Thread.currentThread();
        INSTANCE = this;
    }

    @Override
    public void create() {
        LOGGER = Gdx.app.getApplicationLogger();
        batch = new SpriteBatch();
        shapeDrawer = new ShapeDrawer(batch, generateWhite());
        skin = new Skin(Gdx.files.internal("skins/freezing/freezing-ui.json"));
        font = skin.getFont("font");
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        sceneManager.setScene(new MainMenuScene());
    }

    private TextureRegion generateWhite() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture, 0, 0, 1, 1);
    }

    @Override
    public void render() {
        while (!tasksQueue.isEmpty())
            tasksQueue.poll().run();
        ScreenUtils.clear(0, 0, 0, 1);
        sceneManager.render();
        Gdx.graphics.setTitle(sceneManager.getTitle());
    }

    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float) Constants.WIDTH / (float) Constants.HEIGHT;
        viewport.update(width, height, true);
        Gdx.graphics.setWindowedMode(width, (int) (width / aspectRatio));
    }

    @Override
    public void dispose() {
        ClientGameManager.get().disconnect();
        PvZMultiplayerServer.stop();
        batch.dispose();
        font.dispose();
        skin.dispose();
        System.exit(0);
    }

    public void runLater(Runnable runnable) {
        tasksQueue.add(runnable);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeDrawer getShapeDrawer() {
        return shapeDrawer;
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

    public static Thread getRenderThread() {
        return renderThread;
    }
}
