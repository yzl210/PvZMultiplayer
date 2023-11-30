package cn.leomc.pvzmultiplayer.common.text.component;

//TODO: Implement translatable component
public class TranslatableComponent implements Component {

    private final String key;
    private final Component[] args;

    public TranslatableComponent(String key, Component... args) {
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String get() {
        return key;
    }
}
