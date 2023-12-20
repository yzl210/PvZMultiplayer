package cn.leomc.pvzmultiplayer.client.widget;

import cn.leomc.pvzmultiplayer.client.renderer.GameRenderer;
import cn.leomc.pvzmultiplayer.client.renderer.PlantGameRenderer;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.List;

public class Bar extends HorizontalGroup {
    private final Skin skin;
    private final GameRenderer renderer;

    private final SunCard sunCard;
    private ShovelSlot shovelSlot;

    public Bar(GameRenderer renderer, Skin skin) {
        this.renderer = renderer;
        this.skin = skin;
        this.sunCard = new SunCard(renderer, skin);
        sunCard.setScale(getScaleX() + 0.5f, getScaleY() + 0.5f);
        addActor(sunCard);
        if (renderer instanceof PlantGameRenderer plantGameRenderer) {
            this.shovelSlot = new ShovelSlot(plantGameRenderer);
            shovelSlot.setScale(getScaleX(), getScaleY());
            addActor(shovelSlot);
        }
    }

    public void addEntity(EntityType<?, ?> entity) {
        EntityCard entityCard = new EntityCard(entity, renderer, skin);
        entityCard.setScale(getScaleX(), getScaleY());
        if (shovelSlot == null)
            addActor(entityCard);
        else
            addActorBefore(shovelSlot, entityCard);
        pack();
    }

    public List<EntityCard> getEntityCards() {
        List<EntityCard> cards = new ArrayList<>();
        for (Actor child : getChildren()) {
            if (child instanceof EntityCard)
                cards.add((EntityCard) child);
        }
        return cards;
    }

    @Override
    protected void scaleChanged() {
        sunCard.setScale(getScaleX() + 0.5f, getScaleY() + 0.5f);
        for (Actor child : getChildren())
            if (child instanceof EntityCard)
                child.setScale(getScaleX(), getScaleY());
        pack();
    }
}
