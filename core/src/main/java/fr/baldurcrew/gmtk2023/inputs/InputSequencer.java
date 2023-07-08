package fr.baldurcrew.gmtk2023.inputs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.baldurcrew.gmtk2023.Constants;

import java.util.LinkedList;

public class InputSequencer {

    private static final float MIN_GENERATED_INPUTS_DURATION = 4f;
    private static final float INPUT_QUEUE_VIEWER_WIDTH = Constants.VIEWPORT_WIDTH;
    private static final int INPUT_QUEUE_VIEWER_WIDTH_AS_TICKS = Math.round(MIN_GENERATED_INPUTS_DURATION * 1f / Constants.TIME_STEP);
    private static final float INPUT_QUEUE_VIEWER_HEIGHT = 1.f;

    private final Texture rightTexture;
    private final Texture leftTexture;
    private LinkedList<Input> inputs;
    private float tickCounter;

    public InputSequencer() {
        tickCounter = 0;
        inputs = new LinkedList<>();

        rightTexture = new Texture("right_arrow_pixelated.png");
        rightTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        leftTexture = new Texture("left_arrow_pixelated_blue.png");
        leftTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
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
        // TODO For the random generator: add inputs if total ticks <= MIN_GEN_TICKS
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
                ticksToRender -= tickCounter;
            }
            float inputRenderWidth = INPUT_QUEUE_VIEWER_WIDTH * ((float) ticksToRender / (float) INPUT_QUEUE_VIEWER_WIDTH_AS_TICKS);
            switch (input.type) {
                case Left -> renderInputRegion(batch, leftTexture, renderX, renderY, inputRenderWidth);
                case Right -> renderInputRegion(batch, rightTexture, renderX, renderY, inputRenderWidth);
                case Idle -> {
                }
            }
            renderX += inputRenderWidth;
            renderedTicks += ticksToRender;
            if (renderedTicks >= INPUT_QUEUE_VIEWER_WIDTH_AS_TICKS) break;
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
