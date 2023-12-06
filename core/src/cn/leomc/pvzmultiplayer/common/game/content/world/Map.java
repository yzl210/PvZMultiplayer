package cn.leomc.pvzmultiplayer.common.game.content.world;

import com.badlogic.gdx.math.Vector2;

public record Map(Vector2 plantGridTopLeft, Vector2 plantGridDimension) {

    public static final Map DEFAULT = new Map(new Vector2(115, 50), new Vector2(110, 105));

}
