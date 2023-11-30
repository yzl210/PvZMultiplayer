package cn.leomc.pvzmultiplayer.common.text.component;

//TODO: Implement translatable component
public class TranslatableComponent implements Component {

    private final String key;

    public TranslatableComponent(String key) {
        this.key = key;
    }

    @Override
    public String get() {
        return key;
    }
}
