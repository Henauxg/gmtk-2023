package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.CoreGame;

public class MainMenuScene implements Scene {

    private CoreGame game;

    public MainMenuScene(CoreGame game) {
        this.game = game;
    }

    @Override
    public void handleInputs() {
        if (Gdx.input.isTouched()) {
            game.setScene(new GameScene(game));
            dispose();
        }
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
