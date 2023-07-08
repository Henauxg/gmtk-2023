package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.math.MathUtils;

public enum InputType {
    Left(InputSequencer.UNIT_MOVEMENT_TICKS),
    Right(InputSequencer.UNIT_MOVEMENT_TICKS),
    JumpLeft(InputSequencer.UNIT_JUMP_TICKS),
    JumpRight(InputSequencer.UNIT_JUMP_TICKS),
    Idle(InputSequencer.UNIT_MOVEMENT_TICKS);

    public final int ticks;

    InputType(int ticks) {
        this.ticks = ticks;
    }

    public static InputType getRandom() {
        InputType[] types = values();
        return types[MathUtils.random(0, types.length - 1)];
    }
}
