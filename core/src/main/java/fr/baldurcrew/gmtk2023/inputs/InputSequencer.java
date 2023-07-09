package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.Constants;

import java.util.LinkedList;
import java.util.Objects;

public class InputSequencer {

    public static final int UNIT_MOVEMENT_TICKS = 30;
    public static final int UNIT_JUMP_TICKS = 60;

    private static final float QUEUE_RENDER_WIDTH = Constants.VIEWPORT_WIDTH / 2f;
    private static final float QUEUE_RENDER_HEIGHT = 1.f;
    private static final int QUEUE_INPUTS_RENDER_COUNT = 10;
    private static final float QUEUE_INPUT_RENDER_WIDTH = QUEUE_RENDER_WIDTH / QUEUE_INPUTS_RENDER_COUNT;
    private static final Input FAKE_IDLE_INPUT = new Input(InputType.Idle, 0);
    private final boolean random;
    private LinkedList<Input> inputs;
    private int currentInputTickCounter;
    private int inputId;

    public InputSequencer(boolean generateRandomInputs) {
        this.random = generateRandomInputs;
        this.currentInputTickCounter = 0;
        this.inputId = 0;
        this.inputs = new LinkedList<>();

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
        inputId++;
        final var type = InputType.getRandom();
        return new Input(type, inputId);
    }

    private void addInput(Input input) {
        inputs.add(input);
    }

    public void addInput(InputType type) {
        inputId++;
        inputs.add(new Input(type, inputId));
    }

    public void advance(boolean allowTransition) {
        if (inputs.size() == 0) return;

        if (currentInputTickCounter <= inputs.get(0).type.ticks) {
            currentInputTickCounter++;
        }
        if (currentInputTickCounter >= inputs.get(0).type.ticks && allowTransition) {
            inputs.removeFirst();
            currentInputTickCounter = 0;
        }
        if (random) {
            fillInputQueue(QUEUE_INPUTS_RENDER_COUNT);
        }
    }

    public Input getInput() {
        // No more moves in stock
        if (inputs.size() == 0) return FAKE_IDLE_INPUT;

        // The current move has ended, but we have not been allowed to transition to the next one yet
        if (currentInputTickCounter > inputs.get(0).type.ticks) return FAKE_IDLE_INPUT;

        return inputs.get(0);
    }

    public void render(float deltaTime, Camera camera, SpriteBatch batch) {
        final float startX = camera.viewportWidth / 2f - QUEUE_RENDER_WIDTH / 2f;
        float renderX = startX;
        float renderY = camera.viewportHeight - QUEUE_RENDER_HEIGHT;

        // TODO Animate
        for (int i = 0; i < QUEUE_INPUTS_RENDER_COUNT && i < inputs.size(); i++) {
            final var input = inputs.get(i);
            if (input.type != InputType.Idle) {
                batch.draw(InputResources.getInstance().getTexture(input.type), renderX, renderY, QUEUE_INPUT_RENDER_WIDTH, QUEUE_RENDER_HEIGHT);
            }
            renderX += QUEUE_INPUT_RENDER_WIDTH;
        }
        batch.draw(InputResources.getInstance().bordersTexture, startX, renderY, QUEUE_INPUT_RENDER_WIDTH, QUEUE_RENDER_HEIGHT);
    }

    public static final class Input {
        private final InputType type;
        private final int id;

        public Input(InputType type, int id) {
            this.type = type;
            this.id = id;
        }

        @Override
        public String toString() {
            return "Input{" +
                "type=" + type +
                ", id=" + id +
                '}';
        }

        public InputType type() {
            return type;
        }

        public int id() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Input) obj;
            return Objects.equals(this.type, that.type) &&
                this.id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, id);
        }

    }
}
