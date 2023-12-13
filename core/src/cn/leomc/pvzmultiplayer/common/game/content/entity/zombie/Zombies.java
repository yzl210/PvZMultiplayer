package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombies.NormalZombie;
import com.badlogic.gdx.math.Vector2;

public class Zombies {

    public static final ZombieType<Zombie> NORMAL = ZombieType.builder()
            .id("normal")
            .sun(50)
            .health(10)
            .damage(1)
            .speed(new Vector2(-1f, 0))
            .dimension(new Vector2(120, 200))
            .eggRechargeTicks(750)
            .texture(ZombieState.WALKING, 1)
            .constructor(NormalZombie::new)
            .deserializer(NormalZombie::new)
            .entityClass(Zombie.class)
            .build();

    public static void register() {
        EntityManager.register(NORMAL);
    }

}
