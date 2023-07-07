package fr.baldurcrew.gmtk2023.screens;

import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.CoreGame;
import fr.baldurcrew.gmtk2023.npc.Npc;

public class GameScene implements Scene {

    private final CoreGame game;
    private Npc npc;

    public GameScene(CoreGame coreGame) {
        this.game = coreGame;
        this.npc = new Npc(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2f);
    }

    @Override
    public void handleInputs() {

    }

    @Override
    public void handleDebugInputs() {

    }

    @Override
    public void update() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.spriteBatch.begin();
        game.spriteBatch.setProjectionMatrix(game.camera.combined);
        npc.render(delta, game.spriteBatch);
        game.spriteBatch.end();

        ImGui.ShowDemoWindow();
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
