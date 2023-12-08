package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class PlantSeedCard extends Table {

    private final IntSupplier sun;
    private final Supplier<PlantType<?>> selected;
    private final PlantType<?> plant;
    private final Renderable background;
    private final Renderable plantTexture;
    private final Label label;

    public PlantSeedCard(PlantType<?> plant, IntSupplier sun, Supplier<PlantType<?>> selected, Consumer<PlantType<?>> callback, Skin skin) {
        this.sun = sun;
        this.selected = selected;
        this.background = new FixedTexture("textures/seedpack.png");
        this.plant = plant;
        this.plantTexture = plant.texture();
        this.label = new Label(String.valueOf(plant.sun()), skin);
        label.setColor(Color.BLACK);
        align(Align.bottom).padBottom(5).add(label);

        setTouchable(Touchable.enabled);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selected.get() == plant)
                    callback.accept(null);
                else if (ClientGameManager.get().getSun() >= plant.sun())
                    callback.accept(plant);
            }
        });
    }

    @Override
    public float getPrefWidth() {
        return 100 * getScaleX();
    }

    @Override
    public float getPrefHeight() {
        return 140 * getScaleY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        label.setColor(sun.getAsInt() >= plant.sun() ? Color.BLACK : Color.RED);
        background.render(getX(), getY(), getWidth(), getHeight());
        plantTexture.render(getX() + 10, getY() + (getHeight() / 4), plant.dimension().x * getScaleX(), plant.dimension().y * getScaleY());
        batch.begin();
        super.draw(batch, parentAlpha);
        batch.flush();
        boolean enoughSun = sun.getAsInt() >= plant.sun();
        if (!enoughSun || selected.get() == plant) {
            float alpha = enoughSun ? 0.4f : 0.6f;
            ShapeRenderer shapeRenderer = PvZMultiplayerClient.getInstance().getShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, alpha);
            shapeRenderer.rect(getX() * getScaleX(), (Gdx.graphics.getHeight() - getY()) - getHeight() * getScaleY(), getWidth() * getScaleX(), getHeight() * getScaleY());
            shapeRenderer.end();
        }
    }

    @Override
    protected void scaleChanged() {
        label.setFontScale(getScaleX(), getScaleY());
        pack();
    }

}
