package cn.leomc.pvzmultiplayer.common.game.content.entity.plants.resource;

import cn.leomc.pvzmultiplayer.common.Utils;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plants;
import cn.leomc.pvzmultiplayer.common.game.content.entity.simple.Sun;
import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.ThreadLocalRandom;

public class Sunflower extends Plant {

    private static final int SUN_INTERVAL = Utils.millisToTicks(24000);

    private int sunTimer = SUN_INTERVAL / 2;

    public Sunflower(PlantContext context) {
        super(context);
    }

    public Sunflower(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void tick() {
        super.tick();
        if (sunTimer > 0) {
            sunTimer--;
            return;
        }
        sunTimer = SUN_INTERVAL;
        Sun sun = new Sun(new EntityCreationContext() {
            @Override
            public Vector2 position() {
                return new Vector2(position.x + ThreadLocalRandom.current().nextFloat(-35, 35), position.y + 30);
            }

            @Override
            public World world() {
                return world;
            }
        });
        sun.velocity().set(0, -1f);
        world.addEntity(sun);
    }

    @Override
    public PlantType<?> type() {
        return Plants.SUNFLOWER;
    }
}
