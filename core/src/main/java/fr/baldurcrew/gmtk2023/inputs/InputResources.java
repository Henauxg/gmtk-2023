package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.graphics.Texture;

public class InputResources {
    private static InputResources instance;
    public final Texture bordersTexture;
    private final Texture[] inputTextures;

    public InputResources() {
        inputTextures = new Texture[InputType.values().length - 1];
        inputTextures[InputType.Left.ordinal()] = new Texture("inputs/left_arrow.png");
        inputTextures[InputType.Right.ordinal()] = new Texture("inputs/right_arrow.png");
        inputTextures[InputType.JumpLeft.ordinal()] = new Texture("inputs/left_arrow_jump.png");
        inputTextures[InputType.JumpRight.ordinal()] = new Texture("inputs/right_arrow_jump.png");

        bordersTexture = new Texture("inputs/borders.png");
    }

    public static InputResources getInstance() {
        if (instance == null) {
            instance = new InputResources();
        }

        return instance;
    }

    public Texture getTexture(InputType type) {
        return inputTextures[type.ordinal()];
    }
}
