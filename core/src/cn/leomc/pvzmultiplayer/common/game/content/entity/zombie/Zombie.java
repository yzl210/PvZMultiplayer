package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.client.texture.FixedTexture;
import cn.leomc.pvzmultiplayer.common.Utils;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.simple.ClientEntity;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

public abstract class Zombie extends Entity {

    public static final int ATTACK_INTERVAL = Utils.millisToTicks(1000);
    private int attackTimer = 0;
    protected ZombieState state = ZombieState.IDLE;

    public Zombie(EntityCreationContext context) {
        super(context);
        this.velocity.set(type().speed());
    }

    public Zombie(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        if (state.isDying())
            damage(0.05f);
        else if (state.isDead())
            world.removeEntity(this);
        else
            state = health > type().health() / 2 ? ZombieState.WALKING : ZombieState.WALKING_HALF_HEALTH;
        super.tick();
        attackTimer++;
    }

    @Override
    public void tickCollision() {
        if (!state.isDead()) {
            if (!state.isEating())
                this.velocity.set(type().speed());
            super.tickCollision();
        }
    }

    @Override
    public void onCollide(Entity entity) {
        if (entity instanceof Plant plant) {
            if (!state.isEating())
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

    @Override
    public void damage(double amount) {
        if (state.isDead())
            return;
        health -= amount;
        if (health <= -2)
            state = ZombieState.DEAD;
        else if (!state.isDying() && health <= 0) {
            ZombieState oldState = state;
            state = state.isEating() ? ZombieState.EATING_DYING : ZombieState.WALKING_DYING;
            position.add(type().dimension(oldState).x - type().dimension(state).x, 0);
        }
    }

    public ZombieState getState() {
        return state;
    }

    public void setState(ZombieState state) {
        this.state = state;
    }

    public boolean isDead() {
        return state.isDead();
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
            updateTexture(state, newState);
        state = newState;
    }

    public void updateTexture(ZombieState oldState, ZombieState newState) {
        if (texture instanceof AnimatedTexture animatedTexture) {
            texture = type().texture(newState);
            if ((oldState == ZombieState.WALKING && newState == ZombieState.WALKING_HALF_HEALTH) ||
                    (oldState == ZombieState.EATING && newState == ZombieState.EATING_HALF_HEALTH)) {
                ((AnimatedTexture) texture).setKeyFrame(animatedTexture.getKeyFrame());
            }
        }

        if (oldState != null && oldState.isFullHealth() && newState.isHalfHealth()) {
            Vector2 position = position().cpy();
            if (oldState.isWalking() && newState.isWalking())
                position.add(40, 15);

            ClientEntity entity = new ClientEntity(EntityCreationContext.of(position, world));
            entity.setDimension(new Vector2(7 * 3, 15 * 3));
            entity.setTexture(new FixedTexture("textures/zombies/normal/arm.png"));
            Vector2 offset = new Vector2(0, -15);
            entity.setRenderer(entity.new DroppedPartRenderer(30, offset, Interpolation.pow2In));
            world.addClientEntity(entity);
        }

        if (oldState != null && !oldState.isDying() && newState.isDying()) {
            Vector2 position = position().cpy();
            if (oldState.isWalking() && newState.isWalking())
                position.add(0, 80);

            ClientEntity entity = new ClientEntity(EntityCreationContext.of(position, world));
            entity.setDimension(new Vector2(22 * 3, 20 * 3));
            entity.setTexture(new FixedTexture("textures/zombies/normal/head.png"));
            Vector2 offset = new Vector2(0, -80);
            entity.setRenderer(entity.new DroppedPartRenderer(30, offset, Interpolation.pow2In));
            world.addClientEntity(entity);
        }

        if (newState == ZombieState.DEAD) {
            ClientEntity entity = new ClientEntity(EntityCreationContext.of(position.cpy().add(-70, 0), world));
            entity.setDimension(type().dimension(ZombieState.DEAD));
            entity.setTexture(type().texture(ZombieState.DEAD));
            entity.setRenderer(entity.new DroppedPartRenderer(40, new Vector2(0, 0), Interpolation.linear));
            world.removeEntity(this);
            world.addClientEntity(entity);
        }
    }


    public abstract ZombieType<?> type();
}
