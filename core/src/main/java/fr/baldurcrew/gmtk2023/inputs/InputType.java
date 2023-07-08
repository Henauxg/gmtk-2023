package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.math.MathUtils;

public enum InputType {
    Left,
    Right,
    Idle;

    public static InputType getRandom() {
        InputType[] types = values();
        return types[MathUtils.random(0, types.length - 1)];
    }
}
