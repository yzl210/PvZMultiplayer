package cn.leomc.pvzmultiplayer.client.texture;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AnimatedTexture implements Renderable {

    private static final Map<String, TextureRegion[]> textureRegions = new HashMap<>();

    private final String path;
    private final int frames;
    private final float frameDuration;
    private final Animation.PlayMode playMode;
    private final float alpha;


    private Texture sheet;
    private Animation<TextureRegion> animation;
    private float timeElapsed;

    public AnimatedTexture(String path, int frames) {
        this(path, frames, Animation.PlayMode.LOOP);
    }

    public AnimatedTexture(String path, int frames, Animation.PlayMode playMode) {
        this(path, frames, playMode, 1 / 6f);
    }

    public AnimatedTexture(String path, int frames, Animation.PlayMode playMode, float frameDuration) {
        this(path, frames, playMode, frameDuration, 1);
    }

    public AnimatedTexture(String path, int frames, Animation.PlayMode playMode, float frameDuration, float alpha) {
        this.path = path;
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.playMode = playMode;
        this.alpha = alpha;
    }

    public void load() {
        this.sheet = textures.computeIfAbsent(path, Texture::new);
        TextureRegion[] regions = textureRegions.computeIfAbsent(path, p ->
                TextureRegion.split(sheet, sheet.getWidth() / frames, sheet.getHeight())[0]);
        this.animation = new Animation<>(frameDuration, regions);
        animation.setPlayMode(playMode);
    }

    public void setKeyFrame(int frame) {
        timeElapsed = frame * frameDuration;
    }

    @Override
    public void render(float x, float y) {
        if (animation == null)
            load();
        timeElapsed += Gdx.graphics.getDeltaTime();
        TextureRegion region = animation.getKeyFrame(timeElapsed, true);
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(region, x, y);
        batch.setColor(1, 1, 1, 1);
        batch.end();
    }

    @Override
    public void render(float x, float y, float width, float height) {
        if (animation == null)
            load();
        timeElapsed += Gdx.graphics.getDeltaTime();
        TextureRegion region = animation.getKeyFrame(timeElapsed, true);
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(region, x, y, width, height);
        batch.setColor(1, 1, 1, 1);
        batch.end();
    }
}
