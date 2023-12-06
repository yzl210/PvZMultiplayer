package cn.leomc.pvzmultiplayer.client.texture;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FixedTexture implements Renderable {

    private final String path;

    private Texture texture;

    public FixedTexture(String path) {
        this.path = path;
    }


    @Override
    public void render(float x, float y) {
        if (texture == null)
            texture = new Texture(path);
        render(x, y, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void render(float x, float y, float width, float height) {
        if (texture == null)
            texture = new Texture(path);
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        batch.draw(texture, x, y, width, height);
        batch.end();
    }
}