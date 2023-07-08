package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;

public class InputSequencer {

    private static final float MIN_GENERATED_INPUTS_DURATION = 3f;
    private LinkedList<Input> inputs;
    private float tickCounter;

    public InputSequencer() {
        tickCounter = 0;
        inputs = new LinkedList<>();
    }

    public void addInput(Input input) {
        inputs.add(input);
    }

    public void advance() {
        if (inputs.size() == 0) return;

        tickCounter++;

        if (tickCounter >= inputs.get(0).ticks) {
            inputs.removeFirst();
            tickCounter = 0;
        }
        // TODO Add inputs if total ticks <= MIN_GEN_TICKS
    }

    public InputType getInput() {
        if (inputs.size() == 0) return InputType.Idle;

        return inputs.get(0).type;
    }

    public void render(float deltaTime, SpriteBatch spriteBatch) {
    }

    public record Input(InputType type, int ticks) {
    }
}
