package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.renderer.PlantGameRenderer;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ShovelSlot extends Actor {

    private final PlantGameRenderer renderer;
    private final Renderable emptySlot;
    private final Renderable slot;

    public ShovelSlot(PlantGameRenderer renderer) {
        setSize(70, 44);
        this.renderer = renderer;

        this.emptySlot = new FixedTexture("textures/shovel_slot_empty.png");
        this.slot = new FixedTexture("textures/shovel_slot.png");

        setTouchable(Touchable.enabled);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                renderer.selectShovel(!renderer.isShovelSelected());
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        (renderer.isShovelSelected() ? emptySlot : slot).render(getX(), getY(), getWidth(), getHeight());
        batch.begin();
    }
}
