package cn.leomc.pvzmultiplayer.client.renderer;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Objects;

public class CursorRenderer {

    private final Renderable cursor;
    private final GlyphLayout glyphLayout;

    public CursorRenderer() {
        this.cursor = FixedTexture.of("textures/cursor.png");
        this.glyphLayout = new GlyphLayout(PvZMultiplayerClient.getInstance().getFont(), "");
    }

    public void render() {
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        BitmapFont font = PvZMultiplayerClient.getInstance().getFont();
        ShapeDrawer shapeDrawer = PvZMultiplayerClient.getInstance().getShapeDrawer();

        ClientGameManager.get().getCursors().forEach((name, vector2) -> {
            if (Objects.equals(name, ClientGameManager.get().getName()))
                return;
            cursor.render(vector2.x - 10, vector2.y - 20, 30, 30);
            batch.begin();
            glyphLayout.setText(font, name);

            shapeDrawer.setColor(0, 0, 0, 0.5f);
            shapeDrawer.filledRectangle(vector2.x - glyphLayout.width / 2, vector2.y - 50, glyphLayout.width + 10, glyphLayout.height + 10);
            font.setColor(1, 1, 1, 1);
            font.draw(batch, glyphLayout, vector2.x - glyphLayout.width / 2 + 5, vector2.y - 40 + glyphLayout.height);
            batch.end();
        });
    }

}
