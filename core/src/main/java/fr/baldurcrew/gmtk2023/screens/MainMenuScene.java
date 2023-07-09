package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.xpenatan.imgui.core.ImGui;
import fr.baldurcrew.gmtk2023.CoreGame;

public class MainMenuScene implements Scene {

    private final MainMenu mainMenu;
    private CoreGame game;

    public MainMenuScene(CoreGame game) {
        this.game = game;
        this.mainMenu = new MainMenu();


        //TODO if the button pressed is :
        //      play -> change gamescene and dispose the previous one
        //      option -> open the option menu (nothing to dispose)
        //      exit -> System.exit(0)
        setButtonAction("play", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScene(new GameScene(game));
                dispose();
            }
        });

        setButtonAction("exit", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.exit(0);
            }
        });
    }

    @Override
    public void handleInputs() {

    }

    private void setButtonAction(String name, ChangeListener action) {
        var textButton = mainMenu.getTextButton(name);
        textButton.addListener(action);
    }

    @Override
    public void handleDebugInputs() {

    }

    @Override
    public void update(float timeStep) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //ImGui.ShowDemoWindow();
        mainMenu.render();
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
        mainMenu.dispose();
    }
}
