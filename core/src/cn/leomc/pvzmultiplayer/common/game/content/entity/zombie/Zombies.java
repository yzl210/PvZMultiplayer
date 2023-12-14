package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombies.NormalZombie;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Zombies {

    public static final ZombieType<Zombie> NORMAL = ZombieType.builder()
            .id("normal")
            .sun(50)
            .health(10)
            .damage(1)
            .speed(new Vector2(-1f, 0))
            .dimension(ZombieState.IDLE, new Vector2(28 * 3, 44 * 3))
            .dimension(ZombieState.WALKING, new Vector2(30 * 3, 47 * 3))
            .dimension(ZombieState.WALKING_HALF_HEALTH, new Vector2(30 * 3, 47 * 3))
            .dimension(ZombieState.WALKING_DYING, new Vector2(21 * 3, 32 * 3))
            .dimension(ZombieState.EATING, new Vector2(31 * 3, 44 * 3))
            .dimension(ZombieState.EATING_HALF_HEALTH, new Vector2(31 * 3, 44 * 3))
            .dimension(ZombieState.EATING_DYING, new Vector2(30 * 3, 30 * 3))
            .dimension(ZombieState.DEAD, new Vector2(43 * 3, 31 * 3))
            .eggRechargeTicks(750)
            .texture(ZombieState.IDLE, 5)
            .texture(ZombieState.WALKING, 7)
            .texture(ZombieState.WALKING_HALF_HEALTH, 7)
            .texture(ZombieState.WALKING_DYING, 7)
            .texture(ZombieState.EATING, 7)
            .texture(ZombieState.EATING_HALF_HEALTH, 7)
            .texture(ZombieState.EATING_DYING, 5)
            .texture(ZombieState.DEAD, () -> new AnimatedTexture("textures/zombies/normal/dead.png", 9, Animation.PlayMode.NORMAL))
            .constructor(NormalZombie::new)
            .deserializer(NormalZombie::new)
            .entityClass(Zombie.class)
            .build();

    public static void register() {
        EntityManager.register(NORMAL);
    }

}
