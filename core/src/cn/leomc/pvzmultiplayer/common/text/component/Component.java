package cn.leomc.pvzmultiplayer.common.text.component;

public interface Component {
    String get();

    static Component text(String text) {
        return new TextComponent(text);
    }

    static Component translatable(String key, Component... args) {
        return new TranslatableComponent(key, args);
    }
}
