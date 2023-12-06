package cn.leomc.pvzmultiplayer.common.game.content.entity;

import cn.leomc.pvzmultiplayer.common.game.content.world.World;
import com.badlogic.gdx.math.Vector2;

public interface EntityCreationContext {
    Vector2 position();

    World world();
}
