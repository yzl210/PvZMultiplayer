package cn.leomc.pvzmultiplayer.common.game.content.world;

import cn.leomc.pvzmultiplayer.client.ClientGameManager;
import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityCreationContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.ZombieType;
import cn.leomc.pvzmultiplayer.common.game.content.entity.zombie.Zombies;
import cn.leomc.pvzmultiplayer.common.game.logic.GameSession;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ClientboundAddEntityPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ClientboundRemoveEntityPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ClientboundUpdateEntityPacket;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ClientboundUpdateWorldPacket;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class World {

    private final GameSession session;
    private final cn.leomc.pvzmultiplayer.common.game.content.world.Map map = cn.leomc.pvzmultiplayer.common.game.content.world.Map.DEFAULT;

    private final Map<Integer, Entity> entities = new ConcurrentSkipListMap<>();

    private final Table<Integer, Integer, Plant> plants = HashBasedTable.create();
    private NinePatch background;

    public World(GameSession session) {
        this.session = session;
    }

    public GameSession getGameSession() {
        return session;
    }

    public boolean plant(int x, int y, PlantType<?> type) {
        if (x < 0 || y < 0 || x > 8 || y > 4)
            return false;

        if (plants.contains(x, y))
            return false;

        if (session.getSun() < type.sun())
            return false;

        session.setSun(session.getSun() - type.sun());

        Plant plant = type.create(new PlantContext() {
            @Override
            public Vector2 position() {
                return new Vector2(x * map.plantGridDimension().x + map.plantGridTopLeft().x, y * map.plantGridDimension().y + map.plantGridTopLeft().y);
            }

            @Override
            public World world() {
                return World.this;
            }
        });
        setPlant(x, y, plant);
        return true;
    }

    public void setPlant(int x, int y, Plant plant) {
        plants.put(x, y, plant);
        addEntity(plant);
    }

    public void spawnZombie(int lane, ZombieType<?> type) {
        int y = lane * 100 + 50;
        int x = Constants.WIDTH - 200;
        addEntity(type.create(new EntityCreationContext() {
            @Override
            public Vector2 position() {
                return new Vector2(x, y);
            }

            @Override
            public World world() {
                return World.this;
            }
        }));

    }


    public void addEntity(Entity entity) {
        if (entities.containsKey(entity.id()))
            return;
        if (isClient())
            entity.loadResources();
        entities.put(entity.id(), entity);
        if (isServer())
            ServerManager.get().getPlayerList().sendPacket(new ClientboundAddEntityPacket(entity));
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public void removeEntity(int id) {
        entities.remove(id);
        if (isServer())
            ServerManager.get().getPlayerList().sendPacket(new ClientboundRemoveEntityPacket(id));
    }

    public void removeEntity(Entity entity) {
        removeEntity(entity.id());
    }

    int timer = 0;

    public void tick() {
        entities.values().forEach(Entity::tick);
        entities.forEach((id, entity) -> sync(entity));
        timer++;
        if (timer % 100 == 0)
            spawnZombie(0, Zombies.NORMAL);
        // Add zombie for testing

    }

    public void loadResources() {
        entities.values().forEach(Entity::loadResources);
        background = new NinePatch(new TextureRegion(new Texture("backgrounds/game.png"), 175, 0, 950, 600));
    }

    public void render() {
        SpriteBatch batch = PvZMultiplayerClient.getInstance().getBatch();
        batch.begin();
        background.draw(batch, 0, 0, Constants.WIDTH, Constants.HEIGHT - 75);
        batch.end();
        entities.values().forEach(Entity::render);
    }

    public void write(ByteBuf buf) {
        buf.writeInt(entities.size());
        entities.values().forEach(entity -> EntityManager.write(entity, buf));
    }

    public void read(ByteBuf buf) {
        entities.clear();

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            Entity entity = EntityManager.read(buf);
            entities.put(entity.id(), entity);
        }
    }

    public void sync(Entity entity) {
        ServerManager.get().sendPacket(new ClientboundUpdateEntityPacket(entity));
    }

    public void sync() {
        if (isServer())
            ServerManager.get().getPlayerList().sendPacket(new ClientboundUpdateWorldPacket(this));
    }

    private boolean isServer() {
        return PvZMultiplayerServer.getInstance() != null && Thread.currentThread().equals(PvZMultiplayerServer.getInstance().getServerThread());
    }

    private boolean isClient() {
        return Thread.currentThread().equals(ClientGameManager.get().getClientTickThread());
    }

    private boolean isRenderThread() {
        return Thread.currentThread().equals(PvZMultiplayerClient.getRenderThread());
    }

}
