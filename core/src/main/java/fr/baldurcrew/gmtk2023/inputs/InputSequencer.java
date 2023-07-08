package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import fr.baldurcrew.gmtk2023.Constants;

import java.util.LinkedList;

public class InputSequencer {

    private static final float MIN_GENERATED_INPUTS_DURATION = 4f;
    private static final int MIN_GENERATED_INPUTS_DURATION_AS_TICKS = Math.round(MIN_GENERATED_INPUTS_DURATION * 1f / Constants.TIME_STEP);
    private static final float INPUT_QUEUE_VIEWER_WIDTH = Constants.VIEWPORT_WIDTH;
    private static final float INPUT_QUEUE_VIEWER_HEIGHT = 1.f;

    // TODO Ticks should be multiple of a UNIT_MOVEMENT_TICKS
    private static final int MIN_INPUT_DURATION_TICKS = 30;
    private static final int MAX_INPUT_DURATION_TICKS = 60;

    private final Texture rightTexture;
    private final Texture leftTexture;
    private final boolean random;
    private LinkedList<Input> inputs;
    private int totalTicksInInputList;
    private int currentInputTickCounter;

    public InputSequencer(boolean generateRandomInputs) {
        this.random = generateRandomInputs;
        this.currentInputTickCounter = 0;
        this.totalTicksInInputList = 0;
        this.inputs = new LinkedList<>();

        this.rightTexture = new Texture("right_arrow_pixelated.png");
        this.rightTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.leftTexture = new Texture("left_arrow_pixelated_blue.png");
        this.leftTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        if (generateRandomInputs) {
            fillInputQueue(MIN_GENERATED_INPUTS_DURATION_AS_TICKS);
        }
    }

    private void fillInputQueue(int ticksToFill) {
        while (totalTicksInInputList < ticksToFill) {
            final var input = this.generateRandomInput();
            this.addInput(input);
        }
    }

    private Input generateRandomInput() {
        // TODO Better random generation for input types
        // TODO Jump cooldown
        return new Input(InputType.getRandom(), MathUtils.random(MIN_INPUT_DURATION_TICKS, MAX_INPUT_DURATION_TICKS));
    }

    public void addInput(Input input) {
        inputs.add(input);
        totalTicksInInputList += input.ticks;
    }

    public void advance() {
        if (inputs.size() == 0) return;

        currentInputTickCounter++;

        if (currentInputTickCounter >= inputs.get(0).ticks) {
            final var removedInput = inputs.removeFirst();
            totalTicksInInputList -= removedInput.ticks;
            currentInputTickCounter = 0;
        }
        fillInputQueue(MIN_GENERATED_INPUTS_DURATION_AS_TICKS + currentInputTickCounter);
    }

    public InputType getInput() {
        if (inputs.size() == 0) return InputType.Idle;

        return inputs.get(0).type;
    }

    public void render(float deltaTime, Camera camera, SpriteBatch batch) {
        int renderedTicks = 0;
        float renderY = camera.viewportHeight - INPUT_QUEUE_VIEWER_HEIGHT;
        float renderX = 0;

        for (int i = 0; i < inputs.size(); i++) {
            final var input = inputs.get(i);

            var ticksToRender = input.ticks;
            if (i == 0) {
                ticksToRender -= currentInputTickCounter;
            }
            float inputRenderWidth = INPUT_QUEUE_VIEWER_WIDTH * ((float) ticksToRender / (float) MIN_GENERATED_INPUTS_DURATION_AS_TICKS);
            switch (input.type) {
                case Left -> renderInputRegion(batch, leftTexture, renderX, renderY, inputRenderWidth);
                case Right -> renderInputRegion(batch, rightTexture, renderX, renderY, inputRenderWidth);
                case Idle -> {
                }
            }
            renderX += inputRenderWidth;
            renderedTicks += ticksToRender;
            if (renderedTicks >= MIN_GENERATED_INPUTS_DURATION_AS_TICKS) break;
        }
    }

    private void renderInputRegion(SpriteBatch batch, Texture texture, float renderX, float renderY, float renderWidth) {
        // TODO Change 'u' for the current input region
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, renderWidth, 1f);
        batch.draw(textureRegion, renderX, renderY, renderWidth, INPUT_QUEUE_VIEWER_HEIGHT);
    }

    public record Input(InputType type, int ticks) {
        @Override
        public String toString() {
            return "Input{" + "type=" + type + ", ticks=" + ticks + '}';
        }
    }
}
