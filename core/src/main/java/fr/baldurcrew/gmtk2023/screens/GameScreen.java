package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Screen;

public interface GameScreen extends Screen {
    void handleInputs();

    void handleDebugInputs();

    void update();
}
