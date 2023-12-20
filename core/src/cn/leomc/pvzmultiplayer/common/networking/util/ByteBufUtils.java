package cn.leomc.pvzmultiplayer.common.networking.util;

import cn.leomc.pvzmultiplayer.common.text.component.Component;
import cn.leomc.pvzmultiplayer.common.text.component.TextComponent;
import cn.leomc.pvzmultiplayer.common.text.component.TranslatableComponent;
import com.badlogic.gdx.math.Vector2;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.nio.charset.StandardCharsets;

public class ByteBufUtils {

    public static void writeString(ByteBuf buf, String string) {
        buf.writeInt(string.length());
        buf.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf buf) {
        return buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
    }

    public static void writeComponent(Component component, ByteBuf buf) {
        if (component instanceof TextComponent text) {
            buf.writeByte(0);
            writeString(buf, text.get());
        } else if (component instanceof TranslatableComponent translatable) {
            buf.writeByte(1);
            writeString(buf, translatable.getKey());
        }
    }


    public static Component readComponent(ByteBuf buf) {
        byte b = buf.readByte();
        return switch (b) {
            case 0 -> new TextComponent(readString(buf));
            case 1 -> new TranslatableComponent(readString(buf));
            default -> throw new DecoderException("Unknown component type: " + b);
        };
    }

    public static void writeVector2(ByteBuf buf, Vector2 vector2) {
        buf.writeFloat(vector2.x);
        buf.writeFloat(vector2.y);
    }

    public static Vector2 readVector2(ByteBuf buf) {
        return new Vector2(buf.readFloat(), buf.readFloat());
    }
}

