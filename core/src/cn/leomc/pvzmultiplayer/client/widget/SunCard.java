package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.texture.FixedTextureRegion;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.function.IntSupplier;

public class SunCard extends Table {

    private final IntSupplier sun;
    private final Renderable background;
    private final Label label;

    public SunCard(IntSupplier sun, Skin skin) {
        setSize(76, 87);
        this.sun = sun;
        this.background = new FixedTextureRegion("textures/bar.png", 0, 0, 76, 87);
        this.label = new Label("", skin);
        label.setColor(Color.BLACK);
        align(Align.bottom).padBottom(5).add(label);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        label.setText(String.valueOf(sun.getAsInt()));
        batch.end();
        background.render(getX(), getY(), getWidth(), getHeight());
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void scaleChanged() {
        setWidth(getWidth() * getScaleX());
        setHeight(getHeight() * getScaleY());
        padBottom(5 * getScaleY());
        label.setFontScale(getScaleX(), getScaleY());
    }
}
