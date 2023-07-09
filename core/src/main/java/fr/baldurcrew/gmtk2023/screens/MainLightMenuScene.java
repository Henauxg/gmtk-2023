package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.baldurcrew.gmtk2023.CommonResources;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.CoreGame;

public class MainLightMenuScene implements Scene {

    private final Label.LabelStyle labelStyle;
    private final Label uiText;
    private final Stage stage;
    private CoreGame game;

    public MainLightMenuScene(CoreGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        labelStyle = new Label.LabelStyle();
        labelStyle.font = CommonResources.getInstance().defaultFont;
        uiText = new Label("Touch to begin", labelStyle);
//        uiLabel.setSize(Gdx.graphics.getWidth() / 2f, 2f);
        uiText.setSize(2f, 2f);
        uiText.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 1f / 4f);
        uiText.setAlignment(Align.center);

        stage.addActor(uiText);
    }

    @Override
    public void handleInputs() {
        if (Gdx.input.isTouched()) {
            CommonResources.getInstance().mainMenuMusic.setLooping(true);
            CommonResources.getInstance().mainMenuMusic.setVolume(Constants.DEFAULT_AUDIO_VOLUME);
            CommonResources.getInstance().mainMenuMusic.play();

            game.setScene(new GameScene(game));
            dispose();
        }
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
        stage.draw();
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
        stage.dispose();
    }
}
