package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.baldurcrew.gmtk2023.CoreGame;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.Levels;
import fr.baldurcrew.gmtk2023.utils.NumericRenderer;

public class GameScene implements Scene {

    private final CoreGame game;
    private final NumericRenderer numericRenderer;
    private boolean paused;
    private Level level;

    public GameScene(CoreGame coreGame) {
        this.game = coreGame;
        this.paused = false;
        this.numericRenderer = new NumericRenderer();

        level = Levels.LEVELS[0];

//        for (int i = 1; i < 28; i++) {
//            tilemap.setTile(tilemap.getValidTilePosition(i, 7), TileType.Block);
//        }

//        int tickFactor = 2;
//        for (int i = 0; i < 10; i++) {
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Left, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Right, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Left, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, InputSequencer.UNIT_MOVEMENT_TICKS));
//            inputSequencer.addInput(new InputSequencer.Input(InputType.Right, InputSequencer.UNIT_MOVEMENT_TICKS));
//        }
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

        level.update();
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
