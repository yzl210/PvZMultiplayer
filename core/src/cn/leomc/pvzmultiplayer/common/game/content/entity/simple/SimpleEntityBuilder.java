package cn.leomc.pvzmultiplayer.common.game.content.entity.simple;

import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityBuilder;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityType;
import cn.leomc.pvzmultiplayer.common.game.content.world.Entity;

public class SimpleEntityBuilder<T extends Entity> extends EntityBuilder<SimpleEntityBuilder<T>, T, SimpleEntityType<T>, EntityCreationContext> {

    @Override
    public SimpleEntityType<T> build() {
        EntityType<T, EntityCreationContext> type = super.build();
        return () -> type;
    }
}
