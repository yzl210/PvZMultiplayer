package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import com.badlogic.gdx.math.Vector2;

public class SimpleEntities {

    public static final SimpleEntityType<Pea> PEA = SimpleEntityType.<Pea>builder()
            .id("projectile:pea")
            .texture(() -> new FixedTexture("projectiles/pea.png"))
            .dimension(new Vector2(32, 32))
            .constructor(Pea::new)
            .deserializer(Pea::new)
            .entityClass(Pea.class)
            .build();

    public static final SimpleEntityType<Sun> SUN = SimpleEntityType.<Sun>builder()
            .id("misc:sun")
            .constructor(Sun::new)
            .deserializer(Sun::new)
            .entityClass(Sun.class)
            .build();

    public static void register() {
        EntityManager.register(PEA);
        EntityManager.register(SUN);
    }
}