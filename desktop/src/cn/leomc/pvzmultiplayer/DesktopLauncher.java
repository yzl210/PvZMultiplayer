package cn.leomc.pvzmultiplayer;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.Constants;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);
		config.setWindowSizeLimits(Constants.WIDTH, Constants.HEIGHT, Constants.WIDTH, Constants.HEIGHT);
		new Lwjgl3Application(new PvZMultiplayerClient(), config);
	}
}
