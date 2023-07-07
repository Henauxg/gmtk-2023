package fr.baldurcrew.gmtk2023;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.screens.GameScreen;
import fr.baldurcrew.gmtk2023.screens.MainMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class CoreGame extends Game implements InputProcessor {

    private static final float CAMERA_ZOOM_SPEED = 0.08f;
    public SpriteBatch spriteBatch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public boolean debugMode = true;
    private ImGuiWrapper imGuiWrapper;
    private GameScreen gameScreen;
    private float fixedTimeStepAccumulator;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        imGuiWrapper = new ImGuiWrapper();
        // TODO If we want to handle mouse wheel scroll events, we need to share an input processor with ImGui
//        Gdx.input.setInputProcessor(this);

        this.setScreen(new MainMenuScreen(this));
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
        if (gameScreen != null) gameScreen.render(multipliedDeltaTime);
        imGuiWrapper.render();
    }

    private void handleInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }
        if (this.screen != null) {
            gameScreen.handleInputs();

            if (debugMode) {
                gameScreen.handleDebugInputs();
            }
        }
    }

    private void doPhysicsStep(float deltaTime) {
        // Fixed time step.
        // Max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, Constants.MIN_TIME_STEP);
        fixedTimeStepAccumulator += frameTime;
        while (fixedTimeStepAccumulator >= Constants.TIME_STEP) {
            if (gameScreen != null) gameScreen.update();
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

    public void setScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        super.setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        ImGui.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom = MathUtils.clamp(camera.zoom + CAMERA_ZOOM_SPEED * amountY, 0.5f, 1.f);
        return false;
    }
}
