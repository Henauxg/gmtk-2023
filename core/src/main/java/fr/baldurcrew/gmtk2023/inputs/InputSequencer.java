package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.Constants;

import java.util.LinkedList;

public class InputSequencer {

    public static final int UNIT_MOVEMENT_TICKS = 30;

    private static final float QUEUE_RENDER_WIDTH = Constants.VIEWPORT_WIDTH / 2f;
    private static final float QUEUE_RENDER_HEIGHT = 1.f;
    private static final int QUEUE_INPUTS_RENDER_COUNT = 10;
    private static final float QUEUE_INPUT_RENDER_WIDTH = QUEUE_RENDER_WIDTH / QUEUE_INPUTS_RENDER_COUNT;

    private final Texture rightTexture;
    private final Texture leftTexture;
    private final Texture bordersTexture;
    private final boolean random;
    private LinkedList<Input> inputs;
    private int currentInputTickCounter;

    public InputSequencer(boolean generateRandomInputs) {
        this.random = generateRandomInputs;
        this.currentInputTickCounter = 0;
        this.inputs = new LinkedList<>();

        this.rightTexture = new Texture("right_arrow_pixelated.png");
        this.leftTexture = new Texture("left_arrow_pixelated_blue.png");
        this.bordersTexture = new Texture("borders.png");

        if (generateRandomInputs) {
            fillInputQueue(QUEUE_INPUTS_RENDER_COUNT);
        }
    }

    private void fillInputQueue(int inputsToFill) {
        while (inputs.size() < inputsToFill) {
            final var input = this.generateRandomInput();
            this.addInput(input);
        }
    }

    private Input generateRandomInput() {
        // TODO Better random generation for input types
        // TODO Jump cooldown
        return new Input(InputType.getRandom(), UNIT_MOVEMENT_TICKS);
    }

    public void addInput(Input input) {
        inputs.add(input);
    }

    public void advance() {
        if (inputs.size() == 0) return;

        currentInputTickCounter++;

        if (currentInputTickCounter >= inputs.get(0).ticks) {
            final var removedInput = inputs.removeFirst();
            currentInputTickCounter = 0;
        }
        if (random) {
            fillInputQueue(QUEUE_INPUTS_RENDER_COUNT);
        }
    }

    public InputType getInput() {
        if (inputs.size() == 0) return InputType.Idle;

        return inputs.get(0).type;
    }

    public void render(float deltaTime, Camera camera, SpriteBatch batch) {
        final float startX = camera.viewportWidth / 2f - QUEUE_RENDER_WIDTH / 2f;
        float renderX = startX;
        float renderY = camera.viewportHeight - QUEUE_RENDER_HEIGHT;

        // TODO Animate
        for (int i = 0; i < QUEUE_INPUTS_RENDER_COUNT && i < inputs.size(); i++) {
            final var input = inputs.get(i);
            switch (input.type) {
                case Left -> {
                    batch.draw(leftTexture, renderX, renderY, QUEUE_INPUT_RENDER_WIDTH, QUEUE_RENDER_HEIGHT);
                }
                case Right -> {
                    batch.draw(rightTexture, renderX, renderY, QUEUE_INPUT_RENDER_WIDTH, QUEUE_RENDER_HEIGHT);
                }
                case Idle -> {
                }
            }
            renderX += QUEUE_INPUT_RENDER_WIDTH;
        }
        batch.draw(bordersTexture, startX, renderY, QUEUE_INPUT_RENDER_WIDTH, QUEUE_RENDER_HEIGHT);
    }

    public record Input(InputType type, int ticks) {
        @Override
        public String toString() {
            return "Input{" + "type=" + type + ", ticks=" + ticks + '}';
        }
    }
}
