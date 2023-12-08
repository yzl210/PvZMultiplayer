package cn.leomc.pvzmultiplayer.client.widget;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class HorizontalLabeledTextField extends HorizontalGroup {

    private final Label label;
    private final TextField textField;


    public HorizontalLabeledTextField(String label, String text, Skin skin) {
        this.label = new Label(label, skin);
        this.textField = new TextField(text, skin);

        addActor(this.label);
        addActor(this.textField);
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }

    public String getText() {
        return textField.getText();
    }

}
