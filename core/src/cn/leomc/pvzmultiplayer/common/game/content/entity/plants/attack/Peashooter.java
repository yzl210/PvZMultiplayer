package cn.leomc.pvzmultiplayer.common.game.content.entity.plants.attack;

import cn.leomc.pvzmultiplayer.common.Utils;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.simple.Pea;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

public class Peashooter extends Plant {

    public static final int SHOOT_INTERVAL = Utils.millisToTicks(1500);

    private int shootTimer = 0;

    public Peashooter(PlantContext context) {
        super(context);
    }

    public Peashooter(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();
        if (shootTimer > 0) {
            shootTimer--;
            return;
        }
        shootTimer = SHOOT_INTERVAL;
        world.addEntity(new Pea(new EntityCreationContext() {
            @Override
            public Vector2 position() {
                return new Vector2(position.x + 50, position.y + 40);
            }

            @Override
            public World world() {
                return world;
            }
        }));
    }

    @Override
    public void damage(double amount) {
        System.out.println("peashooter damage " + amount);
        super.damage(amount);
    }

    @Override
    public PlantType<Peashooter> type() {
        return Plants.PEASHOOTER;
    }
}
