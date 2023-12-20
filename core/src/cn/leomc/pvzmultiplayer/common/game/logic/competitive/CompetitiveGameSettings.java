package cn.leomc.pvzmultiplayer.common.game.logic.competitive;

import cn.leomc.pvzmultiplayer.common.game.GameSettings;
import cn.leomc.pvzmultiplayer.common.networking.util.ByteBufUtils;
import cn.leomc.pvzmultiplayer.common.server.ServerManager;
import cn.leomc.pvzmultiplayer.common.server.ServerPlayer;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CompetitiveGameSettings extends GameSettings {

    private final Multimap<Team, String> playerTeam = HashMultimap.create();

    public CompetitiveGameSettings() {
    }

    public CompetitiveGameSettings(ByteBuf buf) {
        read(buf);
        if (playerTeam.isEmpty())
            ServerManager.get().getPlayerList().getPlayers().forEach(player -> addPlayer(player.name()));
    }

    public void setPlayerTeam(String player, Team team) {
        playerTeam.values().remove(player);
        playerTeam.put(team, player);
    }

    public void addPlayer(String player) {
        Team team = Arrays.stream(Team.values()).min((o1, o2) -> Integer.compare(playerTeam.get(o1).size(), playerTeam.get(o2).size())).orElse(Team.PLANTS);
        setPlayerTeam(player, team);
    }

    public List<String> getPlayersNames(Team team) {
        return List.copyOf(playerTeam.get(team));
    }

    public List<ServerPlayer> getPlayers(Team team) {
        return getPlayersNames(team).stream().map(ServerManager.get().getPlayerList()::getPlayer).toList();
    }

    public Team getTeam(String player) {
        return playerTeam.entries().stream().filter(entry -> entry.getValue().equals(player)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public boolean canStart() {
        return true; //TODO: Arrays.stream(Team.values()).noneMatch(team -> playerTeam.get(team).isEmpty());
    }

    @Override
    public void onAddPlayer(ServerPlayer player) {
        addPlayer(player.name());
    }

    @Override
    public void onRemovePlayer(ServerPlayer player) {
        playerTeam.values().remove(player.name());
    }

    @Override
    public GameMode mode() {
        return GameMode.COMPETITIVE;
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeInt(playerTeam.size());
        AtomicInteger i = new AtomicInteger();
        playerTeam.forEach((team, player) -> {
            i.getAndIncrement();
            buf.writeInt(team.ordinal());
            ByteBufUtils.writeString(player, buf);
        });
    }

    public void read(ByteBuf buf) {
        super.read(buf);
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            Team team = Team.values()[buf.readInt()];
            String player = ByteBufUtils.readString(buf);
            playerTeam.put(team, player);
        }
    }
}
