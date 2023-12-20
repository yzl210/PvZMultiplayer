package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.renderer.GameRenderer;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class EntityCard extends Table {

    private final GameRenderer renderer;
    private final EntityType<?, ?> entity;
    private final int cost;
    private final int cooldown;
    private final Renderable background;
    private final Renderable entityTexture;
    private final Label label;

    public EntityCard(EntityType<?, ?> entity, GameRenderer renderer, Skin skin) {
        this.renderer = renderer;
        this.background = new FixedTexture("textures/seedpack.png");
        this.entity = entity;
        this.entityTexture = entity.texture();
        this.cost = renderer.getCost(entity);
        this.cooldown = renderer.getCooldown(entity);
        this.label = new Label(String.valueOf(cost), skin);
        label.setColor(Color.BLACK);
        align(Align.bottom).padBottom(5).add(label);

        setTouchable(Touchable.enabled);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (renderer.getPoints() >= cost)
                    renderer.selectEntity(renderer.getSelectedEntity() == entity ? null : entity);
            }
        });
    }

    public EntityType<?, ?> getEntity() {
        return entity;
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
        boolean enoughPoints = renderer.getPoints() >= cost;
        label.setColor(enoughPoints ? Color.BLACK : Color.RED);
        background.render(getX(), getY(), getWidth(), getHeight());
        entityTexture.render(getX() + 10, getY() + (getHeight() / 4), entity.dimension().x * getScaleX(), entity.dimension().y * getScaleY());
        batch.begin();
        super.draw(batch, parentAlpha);

        float alpha = enoughPoints ? 0.4f : 0.6f;
        if (!enoughPoints || renderer.getSelectedEntity() == entity) {
            ShapeDrawer shapeDrawer = PvZMultiplayerClient.getInstance().getShapeDrawer();
            shapeDrawer.setColor(0, 0, 0, alpha);
            shapeDrawer.filledRectangle(getX(), getY(), getWidth(), getHeight());
        }
        if (ClientGameManager.get().getEntityCooldown(entity) > 0) {
            float cooldownPercent = (float) ClientGameManager.get().getEntityCooldown(entity) / cooldown;
            ShapeDrawer shapeDrawer = PvZMultiplayerClient.getInstance().getShapeDrawer();
            shapeDrawer.setColor(0, 0, 0, alpha);
            shapeDrawer.filledRectangle(getX(), getY() + (getHeight() * (1 - cooldownPercent)), getWidth(), getHeight() * cooldownPercent);
        }
    }

    @Override
    protected void scaleChanged() {
        label.setFontScale(getScaleX(), getScaleY());
        pack();
    }
}
