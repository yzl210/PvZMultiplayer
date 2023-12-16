package cn.leomc.pvzmultiplayer.common.game.content.entity.plants;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.common.Utils;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.attack.Peashooter;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.defense.Wallnut;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.resource.Sunflower;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Plants {

    public static final PlantType<Sunflower> SUNFLOWER = PlantType.<Sunflower>builder()
            .id("sunflower")
            .health(6)
            .sun(50)
            .seedRechargeTicks(Utils.millisToTicks(7500))
            .texture(PlantState.IDLE, () -> new AnimatedTexture("textures/plants/sunflower/idle.png", 6, Animation.PlayMode.LOOP_PINGPONG))
            .dimension(new Vector2(70, 80))
            .constructor(Sunflower::new)
            .deserializer(Sunflower::new)
            .entityClass(Sunflower.class)
            .build();

    public static final PlantType<Peashooter> PEASHOOTER = PlantType.<Peashooter>builder()
            .id("peashooter")
            .health(6)
            .sun(100)
            .seedRechargeTicks(Utils.millisToTicks(7500))
            .texture(PlantState.IDLE, 8)
            .texture(PlantState.ATTACKING, 9)
            .constructor(Peashooter::new)
            .deserializer(Peashooter::new)
            .entityClass(Peashooter.class)
            .build();

    public static final PlantType<Wallnut> WALLNUT = PlantType.<Wallnut>builder()
            .id("wallnut")
            .health(72)
            .sun(50)
            .seedRechargeTicks(Utils.millisToTicks(30000))
            .dimension(new Vector2(78, 90))
            .texture(PlantState.IDLE, 5)
            .constructor(Wallnut::new)
            .deserializer(Wallnut::new)
            .entityClass(Wallnut.class)
            .build();


    public static void register() {
        EntityManager.register(SUNFLOWER);
        EntityManager.register(PEASHOOTER);
        EntityManager.register(WALLNUT);
    }
}
