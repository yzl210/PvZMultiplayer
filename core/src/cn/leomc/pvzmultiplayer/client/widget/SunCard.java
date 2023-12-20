package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.renderer.GameRenderer;
import cn.leomc.pvzmultiplayer.client.texture.FixedTextureRegion;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class SunCard extends Table {

    private final GameRenderer renderer;
    private final Renderable background;
    private final Label label;

    public SunCard(GameRenderer renderer, Skin skin) {
        setSize(76, 87);
        this.renderer = renderer;
        this.background = new FixedTextureRegion("textures/bar.png", 0, 0, 76, 87);
        this.label = new Label("", skin);
        label.setColor(Color.BLACK);
        align(Align.bottom).padBottom(5).add(label);
    }

    @Override
    public float getPrefWidth() {
        return 76 * getScaleX();
    }

    @Override
    public float getPrefHeight() {
        return 87 * getScaleY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        label.setText(String.valueOf(renderer.getPoints()));
        batch.end();
        background.render(getX(), getY(), getWidth(), getHeight());
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void scaleChanged() {
        padBottom(5 * getScaleY());
        label.setFontScale(getScaleX(), getScaleY());
        pack();
    }
}
