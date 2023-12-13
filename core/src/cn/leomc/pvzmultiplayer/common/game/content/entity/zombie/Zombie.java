package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import io.netty.buffer.ByteBuf;

public abstract class Zombie extends Entity {

    public static final int ATTACK_INTERVAL = 20;

    private int attackTimer = 0;

    public Zombie(EntityCreationContext context) {
        super(context);
        this.velocity.set(type().speed());
    }

    public Zombie(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();
        attackTimer++;
    }

    @Override
    public void tickCollision() {
        this.velocity.set(type().speed());
        super.tickCollision();
    }

    @Override
    public void onCollide(Entity entity) {
        if (entity instanceof Plant plant) {
            if (attackTimer >= ATTACK_INTERVAL) {
                attackTimer = 0;
                plant.damage(type().damage());
                if (plant.health() <= 0)
                    ServerManager.get().playSound(Sounds.GULP);
                else
                    ServerManager.get().playSound(Sounds.CHOMP);
            }
            this.velocity.set(0, 0);
        }
    }

    public abstract ZombieType<?> type();
}
