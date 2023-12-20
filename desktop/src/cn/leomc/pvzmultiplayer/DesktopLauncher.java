package cn.leomc.pvzmultiplayer;

import cn.leomc.pvzmultiplayer.client.Constants;
import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {
        if (StartOnFirstThreadHelper.startNewJvmIfRequired())
            return;

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(Constants.FPS);
        config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);
        config.setResizable(true);
        new Lwjgl3Application(new PvZMultiplayerClient(), config);
    }
}
