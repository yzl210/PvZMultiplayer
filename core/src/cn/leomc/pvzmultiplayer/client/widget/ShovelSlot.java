package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.client.texture.Renderable;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ShovelSlot extends Actor {

    private final BooleanSupplier selected;
    private final Renderable emptySlot;
    private final Renderable slot;

    public ShovelSlot(BooleanSupplier selected, Consumer<Boolean> select) {
        setSize(70, 44);
        this.selected = selected;

        this.emptySlot = new FixedTexture("textures/shovel_slot_empty.png");
        this.slot = new FixedTexture("textures/shovel_slot.png");

        setTouchable(Touchable.enabled);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                select.accept(!selected.getAsBoolean());
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        (selected.getAsBoolean() ? emptySlot : slot).render(getX(), getY(), getWidth(), getHeight());
        batch.begin();
    }
}
