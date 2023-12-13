package cn.leomc.pvzmultiplayer.common.game.content.entity.plants.attack;

import cn.leomc.pvzmultiplayer.client.texture.AnimatedTexture;
import cn.leomc.pvzmultiplayer.common.Utils;
import cn.leomc.pvzmultiplayer.common.game.audio.Sounds;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.*;
import cn.leomc.pvzmultiplayer.common.game.content.entity.simple.Pea;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

public class Peashooter extends Plant {

    public static final int SHOOT_INTERVAL = Utils.millisToTicks(1500);

    private int shootTimer = 0;

    private boolean isLastTickShooting = false;

    public Peashooter(PlantContext context) {
        super(context);
    }

    public Peashooter(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();
        isLastTickShooting = false;
        if (world.getZombies(getLane()).isEmpty())
            state = PlantState.IDLE;
        else
            state = PlantState.ATTACKING;
        if (shootTimer > 0 || world.getZombies(getLane()).isEmpty()) {
            shootTimer--;
            return;
        }
        shootTimer = SHOOT_INTERVAL;
        world.addEntity(new Pea(new EntityCreationContext() {
            @Override
            public Vector2 position() {
                return new Vector2(position.x + 40, position.y + 50);
            }

            @Override
            public World world() {
                return world;
            }
        }));
        isLastTickShooting = true;
        ServerManager.get().playSound(Sounds.SHOOT);
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeBoolean(isLastTickShooting);
    }

    @Override
    public void read(ByteBuf buf) {
        super.read(buf);
        isLastTickShooting = buf.readBoolean();
        if (isLastTickShooting && texture instanceof AnimatedTexture animatedTexture)
            animatedTexture.setKeyFrame(2);
    }

    @Override
    public PlantType<Peashooter> type() {
        return Plants.PEASHOOTER;
    }
}
