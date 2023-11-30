package cn.leomc.pvzmultiplayer.client.logic;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.common.server.PvZMultiplayerServer;

public class ClientGameManager {

    private String name = "Player";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ClientGameManager get() {
        return PvZMultiplayerClient.getInstance().getGameManager();
    }

    public void host() {
        PvZMultiplayerServer.start();
    }

    public void connect(String address) {

    }
}
