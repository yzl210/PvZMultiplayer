package cn.leomc.pvzmultiplayer.client.texture;

import cn.leomc.pvzmultiplayer.client.Constants;
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

    private final Texture sheet;
    private final Animation<TextureRegion> animation;
    private float timeElapsed;

    public AnimatedTexture(String path, int frames) {
        this.sheet = textures.computeIfAbsent(path, Texture::new);
        TextureRegion[] regions = textureRegions.computeIfAbsent(path, p ->
                TextureRegion.split(sheet, sheet.getWidth() / frames, sheet.getHeight())[0]);
        this.animation = new Animation<>(1f / Constants.FPS, regions);
    }

    @Override
    public void render(float x, float y) {
        timeElapsed += Gdx.graphics.getDeltaTime();
        TextureRegion region = animation.getKeyFrame(timeElapsed, true);
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        batch.draw(region, x, y);
        batch.end();
    }

    @Override
    public void render(float x, float y, float width, float height) {
        timeElapsed += Gdx.graphics.getDeltaTime();
        TextureRegion region = animation.getKeyFrame(timeElapsed, true);
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        batch.draw(region, x, y, width, height);
        batch.end();
    }
}
