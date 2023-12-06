package cn.leomc.pvzmultiplayer.client.texture;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedTexture implements Renderable {

    private final Animation<TextureRegion> animation;
    private float timeElapsed;

    public AnimatedTexture(String path, int frames) {
        Texture sheet = new Texture(path);
        TextureRegion[][] regions = TextureRegion.split(sheet, sheet.getWidth() / frames, sheet.getHeight());
        this.animation = new Animation<>(1f / Constants.FPS, regions[0]);
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
