package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.baldurcrew.gmtk2023.CoreGame;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.Levels;
import fr.baldurcrew.gmtk2023.utils.NumericRenderer;

import java.util.Arrays;
import java.util.LinkedList;

public class GameScene implements Scene {

    private final CoreGame game;
    private final NumericRenderer numericRenderer;
    private boolean paused;
    private Level level;
    private LinkedList<Level> levels;

    public GameScene(CoreGame coreGame) {
        this.game = coreGame;
        this.paused = false;
        this.numericRenderer = new NumericRenderer();

        levels = new LinkedList<>(Arrays.stream(Levels.LEVELS).toList());
        level = levels.removeFirst();
    }

    @Override
    public void handleInputs() {
        level.handleInputs();
    }


    @Override
    public void handleDebugInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            paused = !paused;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            level.reset();
        }
    }

    @Override
    public void update(float timeStep) {
        if (paused) return;

        final var won = level.update();
        if (won) {
            if (levels.size() > 0) {
                level = levels.removeFirst();
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        level.render(game.spriteBatch, game.debugRenderer, numericRenderer, game.camera, game.debugMode, deltaTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        level.dispose();
    }
}
