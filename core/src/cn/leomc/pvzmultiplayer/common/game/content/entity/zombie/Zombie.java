package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

public abstract class Zombie extends Entity {

    public static final int ATTACK_INTERVAL = 20;
    private int attackTimer = 0;

    protected ZombieState state = ZombieState.WALKING;

    public Zombie(EntityCreationContext context) {
        super(context);
        this.velocity.set(type().speed());
    }

    public Zombie(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        state = ZombieState.WALKING;
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
            state = health > type().health() / 2 ? ZombieState.EATING : ZombieState.EATING_HALF_HEALTH;
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


    public ZombieState getState() {
        return state;
    }

    public void setState(ZombieState state) {
        this.state = state;
    }

    @Override
    public Vector2 getDimension() {
        return state == null ? type().dimension() : type().dimension(state);
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeInt(state.ordinal());
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
        ZombieState newState = ZombieState.values()[buf.readInt()];
        if (newState != state)
            texture = type().texture(newState);
        state = newState;
    }

    public abstract ZombieType<?> type();
}
