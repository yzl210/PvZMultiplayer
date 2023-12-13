package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import io.netty.buffer.ByteBuf;

public abstract class Zombie extends Entity {

    public static final int ATTACK_INTERVAL = 20;

    public Zombie(EntityCreationContext context) {
        super(context);
        this.velocity.set(type().speed());
    }

    public Zombie(ByteBuf buf) {
        super(buf);
    }

    private int timer = 0;

    @Override
    public void tick() {
        super.tick();
        timer++;
    }

    @Override
    public void tickCollision() {
        this.velocity.set(type().speed());
        super.tickCollision();
    }

    @Override
    public void onCollide(Entity entity) {
        if (entity instanceof Plant plant) {
            if (timer >= ATTACK_INTERVAL) {
                timer = 0;
                plant.damage(type().damage());
            }
            this.velocity.set(0, 0);
        }
    }

    public abstract ZombieType<?> type();
}
