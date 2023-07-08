package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.CoreGame;
import fr.baldurcrew.gmtk2023.level.Block;
import fr.baldurcrew.gmtk2023.level.Tilemap;
import fr.baldurcrew.gmtk2023.npc.Npc;

public class GameScene implements Scene {


    private final CoreGame game;
    private final World world;
    private final Tilemap tilemap;
    private Npc npc;
    private Block block;

    public GameScene(CoreGame coreGame) {
        this.game = coreGame;

        world = new World(new Vector2(0, Constants.GRAVITY_VALUE), true);
        this.npc = new Npc(world, Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2f);
        this.block = new Block(world, Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2f, 6f, 0.5f);

        this.tilemap = new Tilemap(Constants.TILE_SIZE.cpy().scl(-1f), Constants.TILE_SIZE.cpy(), Math.round(Constants.VIEWPORT_WIDTH / Constants.TILE_SIZE.x) + 2, Math.round(Constants.VIEWPORT_HEIGHT / Constants.TILE_SIZE.y) + 2);
    }

    @Override
    public void handleInputs() {

    }

    @Override
    public void handleDebugInputs() {

    }

    @Override
    public void update() {
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
        game.spriteBatch.end();

        ImGui.ShowDemoWindow();

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
