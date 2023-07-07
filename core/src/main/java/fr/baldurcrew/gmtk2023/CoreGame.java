package fr.baldurcrew.gmtk2023;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.screens.MainMenuScene;
import fr.baldurcrew.gmtk2023.screens.Scene;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class CoreGame extends Game {

    public SpriteBatch spriteBatch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public boolean debugMode = true;
    private ImGuiWrapper imGuiWrapper;
    private Scene scene;
    private float fixedTimeStepAccumulator;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        imGuiWrapper = new ImGuiWrapper();

        this.setScene(new MainMenuScene(this));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();

        handleInputs();

        // TODO Handle game speed
        final var multipliedDeltaTime = Gdx.graphics.getDeltaTime();
        doPhysicsStep(multipliedDeltaTime);

        imGuiWrapper.prepareRender();
        if (scene != null) scene.render(multipliedDeltaTime);
        imGuiWrapper.render();
    }

    private void handleInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }
        if (this.screen != null) {
            scene.handleInputs();

            if (debugMode) {
                scene.handleDebugInputs();
            }
        }
    }

    private void doPhysicsStep(float deltaTime) {
        // Fixed time step.
        // Max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, Constants.MIN_TIME_STEP);
        fixedTimeStepAccumulator += frameTime;
        while (fixedTimeStepAccumulator >= Constants.TIME_STEP) {
            if (scene != null) scene.update();
            // world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
            fixedTimeStepAccumulator -= Constants.TIME_STEP;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void setScene(Scene scene) {
        this.scene = scene;
        super.setScreen(scene);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        ImGui.dispose();
    }
}
