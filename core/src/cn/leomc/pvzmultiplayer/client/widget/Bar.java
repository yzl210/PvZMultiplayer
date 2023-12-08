package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class Bar extends HorizontalGroup {
    private final Skin skin;
    private final IntSupplier sun;
    private final Supplier<PlantType<?>> selected;
    private final Consumer<PlantType<?>> callback;

    private final SunCard sunCard;

    public Bar(IntSupplier sun, Supplier<PlantType<?>> selected, Consumer<PlantType<?>> callback, Skin skin) {
        this.sun = sun;
        this.selected = selected;
        this.callback = callback;
        this.skin = skin;
        this.sunCard = new SunCard(sun, skin);
        sunCard.setScale(getScaleX() + 0.5f, getScaleY() + 0.5f);
        addActor(sunCard);
    }

    public void addPlant(PlantType<?> plant) {
        PlantSeedCard plantSeedCard = new PlantSeedCard(plant, sun, selected, callback, skin);
        plantSeedCard.setScale(getScaleX(), getScaleY());
        addActor(plantSeedCard);
        pack();
    }

    @Override
    protected void scaleChanged() {
        sunCard.setScale(getScaleX() + 0.5f, getScaleY() + 0.5f);
        for (Actor child : getChildren()) {
            if (child instanceof PlantSeedCard)
                child.setScale(getScaleX(), getScaleY());
        }
        pack();
    }
}
