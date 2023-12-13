package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.client.texture.FixedTextureRegion;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import com.badlogic.gdx.math.Vector2;

public class SimpleEntities {

    public static final SimpleEntityType<Pea> PEA = SimpleEntityType.<Pea>builder()
            .id("pea")
            .texture(() -> new FixedTextureRegion("textures/projectiles/pea.png", 0, 3, 10, 10))
            .dimension(new Vector2(30, 30))
            .constructor(Pea::new)
            .deserializer(Pea::new)
            .entityClass(Pea.class)
            .build();

    public static final SimpleEntityType<Sun> SUN = SimpleEntityType.<Sun>builder()
            .id("sun")
            .texture(() -> new AnimatedTexture("textures/misc/sun.png", 2, 0.5f, 0.76f))
            .dimension(new Vector2(64, 64))
            .constructor(Sun::new)
            .deserializer(Sun::new)
            .entityClass(Sun.class)
            .build();

    public static void register() {
        EntityManager.register(PEA);
        EntityManager.register(SUN);
    }
}
