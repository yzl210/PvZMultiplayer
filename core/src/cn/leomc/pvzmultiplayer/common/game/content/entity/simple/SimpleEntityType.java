package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;

public interface SimpleEntityType<T extends Entity> extends EntityType<T, EntityCreationContext> {
    static <T extends Entity> SimpleEntityBuilder<T> builder() {
        return new SimpleEntityBuilder<>();
    }
}
