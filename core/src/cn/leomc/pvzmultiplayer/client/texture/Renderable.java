package cn.leomc.pvzmultiplayer.client.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public interface Renderable {

    Map<String, Texture> textures = new HashMap<>();


    void render(float x, float y);

    void render(float x, float y, float width, float height);

    void render(float x, float y, float width, float height, Color color);
}
