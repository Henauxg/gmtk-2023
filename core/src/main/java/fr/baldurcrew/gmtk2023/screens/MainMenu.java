package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu {
    private Skin skin;
    private Stage stage;

    public MainMenu() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/skin.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "titlemenu_xxl");
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();

        image = new Image(skin, "IAMTHEPLAYERNOW");
        table1.add(image).pad(5.0f);

        table1.row();
        TextButton textButton = new TextButton("Play", skin);
        textButton.setName("play");
        skin.add("play", textButton);
        table1.add(textButton);

        table1.row();
        textButton = new TextButton("Exit", skin);
        textButton.setName("exit");
        skin.add("exit", textButton);
        table1.add(textButton);
        stack.addActor(table1);
        table.add(stack);
        stage.addActor(table);
    }

    public void render() {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public TextButton getTextButton(String buttonName) {
        return skin.get(buttonName, TextButton.class);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public Skin getSkin() {
        return this.skin;
    }
}
