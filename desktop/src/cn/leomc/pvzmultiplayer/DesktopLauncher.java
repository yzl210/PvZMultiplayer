package cn.leomc.pvzmultiplayer;

import cn.leomc.pvzmultiplayer.client.PvZMultiplayerClient;
import cn.leomc.pvzmultiplayer.client.Constants;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			System.err.println("Uncaught exception in thread " + t.getName());
			e.printStackTrace();
		});
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(Constants.FPS);
		config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT);
		config.setResizable(false);
		new Lwjgl3Application(new PvZMultiplayerClient(), config);
	}
}
