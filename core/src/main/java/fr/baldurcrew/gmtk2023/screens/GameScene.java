package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.CoreGame;
import fr.baldurcrew.gmtk2023.inputs.InputSequencer;
import fr.baldurcrew.gmtk2023.inputs.InputType;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;
import fr.baldurcrew.gmtk2023.level.tiles.types.TileType;
import fr.baldurcrew.gmtk2023.npc.Npc;
import fr.baldurcrew.gmtk2023.utils.Utils;

import java.util.LinkedList;

public class GameScene implements Scene {

    private static final int MAX_PLACED_BLOCKS_COUNT = 3;
    private final CoreGame game;
    private final World world;
    private final Tilemap tilemap;
    private final InputSequencer inputSequencer;
    private Npc npc;
    private LinkedList<Tilemap.TilePosition> placedBlocks;
    private boolean paused;

    public GameScene(CoreGame coreGame) {
        this.game = coreGame;
        this.paused = false;

        world = new World(new Vector2(0, Constants.GRAVITY_VALUE), true);
        this.npc = new Npc(world, Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2f);
        this.inputSequencer = new InputSequencer();

        this.tilemap = new Tilemap(world, Constants.TILE_SIZE.cpy().scl(-1f), Constants.TILE_SIZE.cpy(), Math.round(Constants.VIEWPORT_WIDTH / Constants.TILE_SIZE.x) + 2, Math.round(Constants.VIEWPORT_HEIGHT / Constants.TILE_SIZE.y) + 2);
        placedBlocks = new LinkedList<>();

        tilemap.setTile(tilemap.getValidTilePosition(13, 7), TileType.Block);
        tilemap.setTile(tilemap.getValidTilePosition(14, 7), TileType.Block);
        tilemap.setTile(tilemap.getValidTilePosition(15, 7), TileType.Block);
        tilemap.setTile(tilemap.getValidTilePosition(16, 7), TileType.Block);

        int tickFactor = 2;
        for (int i = 0; i < 10; i++) {
            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, 40 * tickFactor));
            inputSequencer.addInput(new InputSequencer.Input(InputType.Left, 20 * tickFactor));
            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, 20 * tickFactor));
            inputSequencer.addInput(new InputSequencer.Input(InputType.Right, 20 * tickFactor));
            inputSequencer.addInput(new InputSequencer.Input(InputType.Idle, 40 * tickFactor));
            inputSequencer.addInput(new InputSequencer.Input(InputType.Left, 20 * tickFactor));
            inputSequencer.addInput(new InputSequencer.Input(InputType.Right, 20 * tickFactor));
        }
    }

    @Override
    public void handleInputs() {
        if (Gdx.input.justTouched()) {
            final var worldPos = Utils.getInputWorldPosition(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
            final var tilePos = tilemap.getValidTilePosition(worldPos);
            if (tilePos != null) {
                handleTileTouch(tilePos);
            }
        }
    }

    private void handleTileTouch(Tilemap.TilePosition tilePos) {
        final var tileType = tilemap.getTypeAtPosition(tilePos);
        switch (tileType) {
            case Void -> {
                if (placedBlocks.size() >= MAX_PLACED_BLOCKS_COUNT) {
                    var removed = placedBlocks.removeFirst();
                    tilemap.setTile(removed, TileType.Void);
                }
                placedBlocks.add(tilePos);
                tilemap.setTile(tilePos, TileType.Block);
            }
            case Block -> {
            }
        }
    }

    @Override
    public void handleDebugInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            paused = !paused;
        }
    }

    @Override
    public void update(float timeStep) {
        if (paused) return;

        inputSequencer.advance();
        final var input = inputSequencer.getInput();
        npc.update(input);

        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        game.spriteBatch.begin();
        game.spriteBatch.setProjectionMatrix(game.camera.combined);
        tilemap.render(deltaTime, game.spriteBatch);
        npc.render(deltaTime, game.spriteBatch);
        inputSequencer.render(deltaTime, game.camera, game.spriteBatch);
        game.spriteBatch.end();

//        ImGui.ShowDemoWindow();

        if (game.debugMode) {
            game.debugRenderer.render(world, game.camera.combined);
        }
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

    }
}
