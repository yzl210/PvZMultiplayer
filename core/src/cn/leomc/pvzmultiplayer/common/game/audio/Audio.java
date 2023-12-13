package cn.leomc.pvzmultiplayer.common.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface Audio {
    void play();

    void play(float pitch);

    void pause();

    void stop();


    String id();

    static Music music(String path) {
        return new Music(path);
    }

    static Sound sound(String path) {
        return new Sound(path);
    }

    static Multiple multiple(String name) {
        List<Audio> audios = new ArrayList<>();

        for (int i = 1; ; i++) {
            String path = name + "/" + i + ".ogg";
            FileHandle file = Gdx.files.internal("sounds/" + path);
            if (!file.exists())
                break;
            audios.add(new Sound(path));
        }
        return new Multiple(name, audios);
    }

    class Music implements Audio {

        private final String name;
        private com.badlogic.gdx.audio.Music music;

        public Music(String name) {
            this.name = name;
        }

        @Override
        public void play() {
            if (music == null) {
                music = Gdx.audio.newMusic(Gdx.files.internal("sounds/" + name));
                music.setLooping(true);
            }
            music.play();
        }

        @Override
        public void play(float pitch) {
            play();
        }

        @Override
        public void pause() {
            music.pause();
        }

        @Override
        public void stop() {
            music.stop();
        }

        @Override
        public String id() {
            return name;
        }

        public boolean isPlaying() {
            return music != null && music.isPlaying();
        }
    }

    class Sound implements Audio {

        private final String name;
        private com.badlogic.gdx.audio.Sound sound;

        public Sound(String name) {
            this.name = name;
        }

        @Override
        public void play() {
            play(1f);
        }

        @Override
        public void play(float pitch) {
            if (sound == null)
                sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + name));
            sound.play(1f, pitch, 0f);
        }

        @Override
        public void pause() {
            sound.pause();
        }

        @Override
        public void stop() {
            sound.stop();
        }

        @Override
        public String id() {
            return name;
        }
    }

    class Multiple implements Audio {

        private final String name;
        private final List<Audio> audios;

        public Multiple(String name, Audio... audios) {
            this(name, List.of(audios));
        }

        public Multiple(String name, List<Audio> audios) {
            this.name = name;
            this.audios = audios;
        }


        @Override
        public void play() {
            if (audios.isEmpty())
                return;
            audios.get(ThreadLocalRandom.current().nextInt(audios.size())).play();
        }

        @Override
        public void play(float pitch) {
            if (audios.isEmpty())
                return;
            audios.get(ThreadLocalRandom.current().nextInt(audios.size())).play(pitch);
        }

        @Override
        public void pause() {
            for (Audio audio : audios)
                audio.pause();
        }

        @Override
        public void stop() {
            for (Audio audio : audios)
                audio.stop();
        }

        @Override
        public String id() {
            return name;
        }
    }

    class RandomPitch implements Audio {

        private final Audio audio;
        private final float minPitch;
        private final float maxPitch;

        public RandomPitch(Audio audio, float minPitch, float maxPitch) {
            this.audio = audio;
            this.minPitch = minPitch;
            this.maxPitch = maxPitch;
        }

        @Override
        public void play() {
            audio.play(ThreadLocalRandom.current().nextFloat(minPitch, maxPitch));
        }

        @Override
        public void play(float pitch) {
            audio.play(pitch);
        }

        @Override
        public void pause() {
            audio.pause();
        }

        @Override
        public void stop() {
            audio.stop();
        }

        @Override
        public String id() {
            return audio.id();
        }
    }

}
