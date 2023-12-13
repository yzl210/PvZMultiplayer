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
            .dimension(ZombieState.IDLE, new Vector2(30 * 3, 47 * 3))
            .dimension(ZombieState.WALKING, new Vector2(30 * 3, 47 * 3))
            .dimension(ZombieState.EATING, new Vector2(31 * 3, 44 * 3))
            .eggRechargeTicks(750)
            .texture(ZombieState.IDLE, 7)
            .texture(ZombieState.WALKING, 7)
            .texture(ZombieState.EATING, 7)
            .constructor(NormalZombie::new)
            .deserializer(NormalZombie::new)
            .entityClass(Zombie.class)
            .build();

    public static void register() {
        EntityManager.register(NORMAL);
    }

}
