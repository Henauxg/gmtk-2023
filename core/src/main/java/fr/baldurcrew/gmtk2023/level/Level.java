package fr.baldurcrew.gmtk2023.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.inputs.InputSequencer;
import fr.baldurcrew.gmtk2023.inputs.InputType;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;
import fr.baldurcrew.gmtk2023.level.tiles.types.TileType;
import fr.baldurcrew.gmtk2023.npc.Npc;
import fr.baldurcrew.gmtk2023.physics.WorldContactListener;
import fr.baldurcrew.gmtk2023.utils.NumericRenderer;
import fr.baldurcrew.gmtk2023.utils.Utils;

import java.util.LinkedList;


public class Level implements Disposable {

    public static final int MAX_PLACED_BLOCKS_COUNT = 3;
    private static final Vector2 PLACED_BLOCK_NUMBER_SIZE = Constants.TILE_SIZE.cpy().scl(0.5f);

    //    public final Tilemap tilemap;
    //TODO Way to give tiles to Level
    public final boolean isRandom;
    public final int startingTileX;
    public final int startingTileY;

    private final World world;
    private final WorldContactListener worldContactListener;
    private LinkedList<Tilemap.TilePosition> placedBlocks;
    private Tilemap tilemap;
    private InputSequencer inputSequencer;
    private Npc npc;
    //    public final Rectangle endArea; // Optional

    public Level(boolean isRandom, int startingTileX, int startingTileY) {
//        this.tilemap = tilemap;
        this.isRandom = isRandom;
        this.startingTileX = startingTileX;
        this.startingTileY = startingTileY;

        this.world = new World(new Vector2(0, Constants.GRAVITY_VALUE), true);
        this.worldContactListener = new WorldContactListener();
        this.world.setContactListener(worldContactListener);

        this.placedBlocks = new LinkedList<>();
        this.inputSequencer = new InputSequencer(isRandom);
        this.tilemap = new Tilemap(world, Constants.TILE_SIZE.cpy().scl(-1f), Constants.TILE_SIZE.cpy(), Math.round(Constants.VIEWPORT_WIDTH / Constants.TILE_SIZE.x) + 2, Math.round(Constants.VIEWPORT_HEIGHT / Constants.TILE_SIZE.y) + 2);
        this.npc = new Npc(world, tilemap.getWorldPosition(startingTileX, startingTileY));
        worldContactListener.addListener(npc);
    }

    public Level(boolean isRandom, int startingTileX, int startingTileY, int endZoneX1, int endZoneY1, int endZoneX2, int endZoneY2) {
        this(isRandom, startingTileX, startingTileY);
//        this.endArea = endArea;
    }

    public void reset() {
        this.placedBlocks = new LinkedList<>();
        this.inputSequencer = new InputSequencer(isRandom);
        this.tilemap.dispose();
        this.tilemap = new Tilemap(world, Constants.TILE_SIZE.cpy().scl(-1f), Constants.TILE_SIZE.cpy(), Math.round(Constants.VIEWPORT_WIDTH / Constants.TILE_SIZE.x) + 2, Math.round(Constants.VIEWPORT_HEIGHT / Constants.TILE_SIZE.y) + 2);
        worldContactListener.clear();
        this.npc.dispose();
        this.npc = new Npc(world, tilemap.getWorldPosition(startingTileX, startingTileY));
        worldContactListener.addListener(npc);
    }

    public void render(SpriteBatch spriteBatch, Box2DDebugRenderer debugRenderer, NumericRenderer numericRenderer, Camera camera, boolean debugMode, float deltaTime) {
        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);
        tilemap.render(deltaTime, spriteBatch);
        // TODO Render green blinking block under cursor ? (wont work on phones)
        // TODO + Red blinking block for the next block that will disappear
        for (int i = 0; i < placedBlocks.size(); i++) {
            final var worldPos = tilemap.getWorldPosition(placedBlocks.get(i));
            numericRenderer.renderNumber(spriteBatch, i + 1, worldPos, PLACED_BLOCK_NUMBER_SIZE);
        }
        npc.render(deltaTime, spriteBatch);
        inputSequencer.render(deltaTime, camera, spriteBatch);
        spriteBatch.end();

        if (debugMode) {
            debugRenderer.render(world, camera.combined);
        }

        renderUi();
    }

    private void renderUi() {
        ImGui.Begin("Debug");

        ImGui.Separator();
        ImGui.Text("Generate inputs");
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
        ImGui.Separator();
        ImGui.Text("Level");
        if (ImGui.Button("Reset")) {
            this.reset();
        }

        ImGui.End();
    }

    public void update() {
        inputSequencer.advance(npc.isTouchingGround());
        final var input = inputSequencer.getInput();
        npc.update(input);

        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
    }

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
    public void dispose() {
        world.dispose();
        tilemap.dispose();
    }
}
