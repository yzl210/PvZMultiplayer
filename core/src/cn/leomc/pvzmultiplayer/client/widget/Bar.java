package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.List;

public class Bar extends HorizontalGroup {
    private final Skin skin;
    private final BarCallback callback;

    private final SunCard sunCard;
    private final ShovelSlot shovelSlot;

    public Bar(BarCallback callback, Skin skin) {
        this.callback = callback;
        this.skin = skin;
        this.sunCard = new SunCard(callback::sun, skin);
        sunCard.setScale(getScaleX() + 0.5f, getScaleY() + 0.5f);
        addActor(sunCard);
        this.shovelSlot = new ShovelSlot(callback::shovelSelected, callback::selectShovel);
        shovelSlot.setScale(getScaleX(), getScaleY());
        addActor(shovelSlot);
    }

    public void addPlant(PlantType<?> plant) {
        PlantSeedCard plantSeedCard = new PlantSeedCard(plant, callback::sun, callback::selectedPlant, callback::selectPlant, skin);
        plantSeedCard.setScale(getScaleX(), getScaleY());
        addActorBefore(shovelSlot, plantSeedCard);
        pack();
    }

    public List<PlantSeedCard> getPlantSeeds() {
        List<PlantSeedCard> cards = new ArrayList<>();
        for (Actor child : getChildren()) {
            if (child instanceof PlantSeedCard)
                cards.add((PlantSeedCard) child);
        }
        return cards;
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

    public interface BarCallback {
        int sun();

        void selectPlant(PlantType<?> plant);

        PlantType<?> selectedPlant();

        boolean shovelSelected();

        void selectShovel(boolean shovel);
    }

}
