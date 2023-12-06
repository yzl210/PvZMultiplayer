package cn.leomc.pvzmultiplayer.common.game.content.entity.plants;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.common.Utils;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.attack.Peashooter;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.resource.Sunflower;
import com.badlogic.gdx.math.Vector2;

public class Plants {

    public static final PlantType<Sunflower> SUNFLOWER = PlantType.<Sunflower>builder()
            .id("sunflower")
            .health(6)
            .sun(50)
            .seedRechargeTicks(Utils.millisToTicks(7500))
            .constructor(Sunflower::new)
            .deserializer(Sunflower::new)
            .entityClass(Sunflower.class)
            .build();

    public static final PlantType<Peashooter> PEASHOOTER = PlantType.<Peashooter>builder()
            .id("peashooter")
            .health(6)
            .sun(100)
            .seedRechargeTicks(Utils.millisToTicks(7500))
            .dimension(new Vector2(80, 80))
            .texture(PlantState.IDLE, () -> new AnimatedTexture("plants/peashooter/idle.png", 1))
            .constructor(Peashooter::new)
            .deserializer(Peashooter::new)
            .entityClass(Peashooter.class)
            .build();


    public static void register() {
        EntityManager.register(SUNFLOWER);
        EntityManager.register(PEASHOOTER);
    }
}
