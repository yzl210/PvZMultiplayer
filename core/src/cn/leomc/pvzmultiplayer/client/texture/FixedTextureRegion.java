package cn.leomc.pvzmultiplayer.client.texture;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FixedTextureRegion implements Renderable {
    private final TextureRegion region;

    public FixedTextureRegion(String path, int x, int y, int width, int height) {
        this.region = new TextureRegion(textures.computeIfAbsent(path, Texture::new), x, y, width, height);
    }

    @Override
    public void render(float x, float y) {
        render(x, y, region.getRegionWidth(), region.getRegionHeight());
    }

    @Override
    public void render(float x, float y, float width, float height) {
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        batch.draw(region, x, y, width, height);
        batch.end();
    }

}
