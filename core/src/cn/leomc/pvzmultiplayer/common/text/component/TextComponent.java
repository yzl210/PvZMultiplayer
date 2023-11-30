package cn.leomc.pvzmultiplayer.common.text.component;

public class TextComponent implements Component {

    private final String text;

    public TextComponent(String text) {
        this.text = text;
    }

    @Override
    public String get() {
        return text;
    }
}
