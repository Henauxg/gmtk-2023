package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.CoreGame;
import fr.baldurcrew.gmtk2023.inputs.InputSequencer;
import fr.baldurcrew.gmtk2023.inputs.InputType;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;
import fr.baldurcrew.gmtk2023.level.tiles.types.TileType;
import fr.baldurcrew.gmtk2023.npc.Npc;
import fr.baldurcrew.gmtk2023.physics.WorldContactListener;
import fr.baldurcrew.gmtk2023.utils.NumericRenderer;
import fr.baldurcrew.gmtk2023.utils.Utils;

import java.util.LinkedList;

public class GameScene implements Scene {

    private static final int MAX_PLACED_BLOCKS_COUNT = 3;
    private static final Vector2 PLACED_BLOCK_NUMBER_SIZE = Constants.TILE_SIZE.cpy().scl(0.5f);
    private final CoreGame game;
    private final World world;
    private final Tilemap tilemap;
    private final InputSequencer inputSequencer;
    private final NumericRenderer numericRenderer;
    private final WorldContactListener worldContactListener;
    private Npc npc;
    private LinkedList<Tilemap.TilePosition> placedBlocks;
    private boolean paused;

    public GameScene(CoreGame coreGame) {
        this.game = coreGame;
        this.paused = false;
        this.numericRenderer = new NumericRenderer();

        this.world = new World(new Vector2(0, Constants.GRAVITY_VALUE), true);
        this.worldContactListener = new WorldContactListener();
        this.world.setContactListener(worldContactListener);
        this.inputSequencer = new InputSequencer(false);
        this.tilemap = new Tilemap(world, Constants.TILE_SIZE.cpy().scl(-1f), Constants.TILE_SIZE.cpy(), Math.round(Constants.VIEWPORT_WIDTH / Constants.TILE_SIZE.x) + 2, Math.round(Constants.VIEWPORT_HEIGHT / Constants.TILE_SIZE.y) + 2);
        placedBlocks = new LinkedList<>();
        this.npc = new Npc(world, tilemap.getWorldPosition(14, 8));
        worldContactListener.addListener(npc);

        for (int i = 1; i < 28; i++) {
            tilemap.setTile(tilemap.getValidTilePosition(i, 7), TileType.Block);
        }

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
                final var index = placedBlocks.indexOf(tilePos);
                if (index >= 0) {
                    // Refresh the block in the FIFO
                    placedBlocks.remove(index);
                    placedBlocks.add(tilePos);
                }
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

        inputSequencer.advance(npc.isTouchingGround());
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
        // TODO Render green blinking block under cursor ? (wont work on phones)
        // TODO + Red blinking block for the next block that will disappear
        for (int i = 0; i < placedBlocks.size(); i++) {
            final var worldPos = tilemap.getWorldPosition(placedBlocks.get(i));
            numericRenderer.renderNumber(game.spriteBatch, i + 1, worldPos, PLACED_BLOCK_NUMBER_SIZE);
        }
        npc.render(deltaTime, game.spriteBatch);
        inputSequencer.render(deltaTime, game.camera, game.spriteBatch);
        game.spriteBatch.end();

        if (game.debugMode) {
            game.debugRenderer.render(world, game.camera.combined);
        }

        renderUi();
    }

    private void renderUi() {
        ImGui.Begin("Debug");
        if (ImGui.Button("Idle")) {
            inputSequencer.addInput(InputType.Idle);
        }
        if (ImGui.Button("Right")) {
            inputSequencer.addInput(InputType.Right);
        }
        if (ImGui.Button("Left")) {
            inputSequencer.addInput(InputType.Left);
        }
        if (ImGui.Button("Jump Right")) {
            inputSequencer.addInput(InputType.JumpRight);
        }
        if (ImGui.Button("Jump left")) {
            inputSequencer.addInput(InputType.JumpLeft);
        }
        ImGui.End();
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
        world.dispose();
        tilemap.dispose();
    }
}
