package cn.leomc.pvzmultiplayer.common.game.audio;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private static final Map<String, Audio> audios = new HashMap<>();


    public static void register(Audio audio) {
        audios.put(audio.id(), audio);
    }

    public static Audio get(String name) {
        return audios.get(name);
    }

    static {
        Sounds.register();
    }

}
