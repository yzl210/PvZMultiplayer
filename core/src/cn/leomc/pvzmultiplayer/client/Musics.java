package cn.leomc.pvzmultiplayer.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Musics {

    public static final Music MENU = Gdx.audio.newMusic(Gdx.files.internal("sounds/mainmenu.mp3"));

    static {
        MENU.setLooping(true);
    }

}
