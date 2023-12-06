package cn.leomc.pvzmultiplayer.common.game.content.world;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.common.game.content.entity.EntityManager;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.Plant;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantContext;
import cn.leomc.pvzmultiplayer.common.game.content.entity.plants.PlantType;
import cn.leomc.pvzmultiplayer.common.game.logic.GameSession;
import cn.leomc.pvzmultiplayer.common.networking.packet.world.ClientboundAddEntityPacket;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final GameSession session;
    private final cn.leomc.pvzmultiplayer.common.game.content.world.Map map = cn.leomc.pvzmultiplayer.common.game.content.world.Map.DEFAULT;

    private final Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    private final Table<Integer, Integer, Plant> plants = HashBasedTable.create();
    private NinePatch background;

    public World(GameSession session) {
        this.session = session;
    }

    public GameSession getGameSession() {
        return session;
    }

    public boolean plant(int x, int y, PlantType<?> type) {
        System.out.println("Planting at " + x + ", " + y);
        if (plants.contains(x, y))
            return false;

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


    public void addEntity(Entity entity) {
        if (!entities.containsKey(entity.id()))
            entities.put(entity.id(), entity);
        if (isServer())
            ServerManager.get().sendPacket(new ClientboundAddEntityPacket(entity));
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }


    public void removeEntity(Entity entity) {
        entities.remove(entity.id());
    }

    public void remove(Entity entity) {
        entities.remove(entity.id());
    }

    public void tick() {
        entities.values().forEach(Entity::tick);
        entities.forEach((id, entity) -> sync(entity));
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

}
