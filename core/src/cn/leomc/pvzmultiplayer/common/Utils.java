package cn.leomc.pvzmultiplayer.common;

public class Utils {

    public static int millisToTicks(int millis) {
        return millis * Constants.TPS / 1000;
    }

}
