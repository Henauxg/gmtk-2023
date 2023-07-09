package fr.baldurcrew.gmtk2023.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.CommonResources;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.inputs.InputSequencer;
import fr.baldurcrew.gmtk2023.inputs.InputType;
import fr.baldurcrew.gmtk2023.level.cutscene.Cutscene;
import fr.baldurcrew.gmtk2023.level.tiles.TileRect;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;
import fr.baldurcrew.gmtk2023.level.tiles.types.TileType;
import fr.baldurcrew.gmtk2023.npc.Npc;
import fr.baldurcrew.gmtk2023.npc.NpcResources;
import fr.baldurcrew.gmtk2023.physics.WorldContactListener;
import fr.baldurcrew.gmtk2023.utils.NumericRenderer;
import fr.baldurcrew.gmtk2023.utils.Utils;

import java.util.LinkedList;
import java.util.List;


public class Level implements Disposable {

    public static final int MAX_PLACED_BLOCKS_COUNT = 3;
    private static final Vector2 PLACED_BLOCK_NUMBER_SIZE = Constants.TILE_SIZE.cpy().scl(0.5f);

    //    public final Tilemap tilemap;
    //TODO Way to give tiles to Level
    public final boolean isRandom;
    public final int startingTileX;
    public final int startingTileY;

    private final Stage stage;
    private final World world;
    private final WorldContactListener worldContactListener;
    private final List<InputType> levelInputs;
    private final List<Tilemap.TilePosition> levelBlocks;
    private final Cutscene startCutscene;
    private final Texture endAreaTileTexture;
    private final Sound blockPlaceSound;
    private final Label.LabelStyle labelStyle;
    private final Label uiLabel;
    private TileRect endArea; // Optional
    private LinkedList<Tilemap.TilePosition> placedBlocks;
    private Tilemap tilemap;
    private InputSequencer inputSequencer;
    private Npc npc;
    private boolean started;
    private boolean renderInputQueue;
    private boolean renderEndArea;
    private boolean levelLost;

    private Level(boolean random, int startingTileX, int startingTileY, List<Tilemap.TilePosition> blocks, Cutscene startCutscene) {
        this.isRandom = random;
        this.startingTileX = startingTileX;
        this.startingTileY = startingTileY;

        this.stage = new Stage(new ScreenViewport());
        this.world = new World(new Vector2(0, Constants.GRAVITY_VALUE), true);
        this.worldContactListener = new WorldContactListener();
        this.world.setContactListener(worldContactListener);
        this.levelInputs = new LinkedList<>();
        this.levelBlocks = blocks;
        this.startCutscene = startCutscene;
        this.endAreaTileTexture = CommonResources.getInstance().greenTileOverlayTexture;
        this.blockPlaceSound = NpcResources.getInstance().blockPlaceSound;
        this.started = false;
        this.renderInputQueue = false;
        this.renderEndArea = false;

        labelStyle = new Label.LabelStyle();
        labelStyle.font = CommonResources.getInstance().defaultFont;
        uiLabel = new Label("", labelStyle);
//        uiLabel.setSize(Gdx.graphics.getWidth() / 2f, 2f);
        uiLabel.setSize(2f, 2f);
        uiLabel.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 1f / 4f);
        uiLabel.setAlignment(Align.center);

        stage.addActor(uiLabel);
    }

    public Level(int startingTileX, int startingTileY, List<Tilemap.TilePosition> blocks, Cutscene startCutscene) {
        this(true, startingTileX, startingTileY, blocks, startCutscene);
        // TODO tilemap data;
        reset();
    }

    public Level(int startingTileX, int startingTileY, List<Tilemap.TilePosition> blocks, Cutscene startCutscene, TileRect endArea, List<InputType> inputs) {
        this(false, startingTileX, startingTileY, blocks, startCutscene);
        this.endArea = endArea;
        this.levelInputs.addAll(inputs);

        reset();
    }

    public void reset() {
        this.levelLost = false;
        this.placedBlocks = new LinkedList<>();
        this.inputSequencer = new InputSequencer(isRandom);
        this.uiLabel.setText("");

        if (this.tilemap != null) {
            this.tilemap.dispose();
        }
        this.tilemap = new Tilemap(world, Constants.TILE_SIZE.cpy().scl(-1f), Constants.TILE_SIZE.cpy(), Math.round(Constants.VIEWPORT_WIDTH / Constants.TILE_SIZE.x) + 2, Math.round(Constants.VIEWPORT_HEIGHT / Constants.TILE_SIZE.y) + 2);
        worldContactListener.clear();

        this.levelBlocks.forEach(blockPos -> tilemap.setTile(blockPos, TileType.Block));

        if (this.npc != null) {
            this.npc.dispose();
        }
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
        if (renderEndArea && endArea != null) {
            tilemap.renderRect(spriteBatch, endAreaTileTexture, endArea);
        }

        npc.render(deltaTime, spriteBatch);
        if (renderInputQueue) {
            inputSequencer.render(deltaTime, camera, spriteBatch);
        }
        spriteBatch.end();

        if (!started) {
            started = startCutscene.render(spriteBatch, deltaTime, this);
            if (started) {
                start();
            }
        }
        stage.draw();

        if (debugMode) {
            debugRenderer.render(world, camera.combined);
            renderDebugUi();
        }
    }

    private void start() {
        renderInputQueue = true;
        renderEndArea = true;
        this.levelInputs.forEach(input -> this.inputSequencer.addInput(input));
    }

    private void renderDebugUi() {
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

    public boolean update() {
        if (levelLost) return false;

        inputSequencer.advance(npc.isTouchingGround());
        final var input = inputSequencer.getInput();
        final var isDead = npc.update(input);
        if (isDead) {
            levelLost = true;
            uiLabel.setText("You lost, tap to retry");
        }

        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);

        return tilemap.isInside(npc.getPosition(), endArea);
    }

    public void handleInputs() {
        if (!levelLost && started && Gdx.input.justTouched()) {
            final var worldPos = Utils.getInputWorldPosition(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
            final var tilePos = tilemap.getValidTilePosition(worldPos);
            if (tilePos != null) {
                handleTileTouch(tilePos);
            }
        }
        if (levelLost && Gdx.input.justTouched()) {
            this.reset();
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
                blockPlaceSound.play(Constants.DEFAULT_AUDIO_VOLUME);
            }
            case Block -> {
                final var index = placedBlocks.indexOf(tilePos);
                if (index >= 0) {
                    // Refresh the block in the FIFO
                    placedBlocks.remove(index);
                    placedBlocks.add(tilePos);
                    blockPlaceSound.play(Constants.DEFAULT_AUDIO_VOLUME);
                }
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        tilemap.dispose();
    }

    public void addInputs(List<InputType> inputs) {
        inputs.forEach(input -> inputSequencer.addInput(input));
    }

    public void showInputQueue() {
        this.renderInputQueue = true;
    }

    public void showEndArea() {
        this.renderEndArea = true;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void addBlock(Tilemap.TilePosition pos) {
        this.tilemap.setTile(pos, TileType.Block);
    }
}
