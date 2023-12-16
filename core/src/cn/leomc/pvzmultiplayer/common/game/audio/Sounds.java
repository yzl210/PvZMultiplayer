package cn.leomc.pvzmultiplayer.common.game.audio;

public class Sounds {

    public static final Audio.Music MAIN_MENU = Audio.music("mainmenu.mp3");
    public static final Audio SEED_SELECT = Audio.sound("seed_select.ogg");
    public static final Audio PLANT = Audio.multiple("plant");
    public static final Audio CHOMP = Audio.multiple("chomp");
    public static final Audio GULP = Audio.sound("gulp.ogg");
    public static final Audio SHOOT = Audio.sound("shoot.ogg");
    public static final Audio SPLAT = Audio.multiple("splat");
    public static final Audio SUN_COLLECT = new Audio.RandomPitch(Audio.sound("sun_collect.ogg"), 0.75f, 1.5f);
    public static final Audio SHOVEL = Audio.sound("shovel.ogg");


    public static void register() {
        AudioManager.register(MAIN_MENU);
        AudioManager.register(SEED_SELECT);
        AudioManager.register(PLANT);
        AudioManager.register(CHOMP);
        AudioManager.register(GULP);
        AudioManager.register(SHOOT);
        AudioManager.register(SPLAT);
        AudioManager.register(SUN_COLLECT);
        AudioManager.register(SHOVEL);
    }

}
