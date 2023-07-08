package fr.baldurcrew.gmtk2023.screens;

import com.badlogic.gdx.Screen;

public interface Scene extends Screen {
    void handleInputs();

    void handleDebugInputs();

    void update(float timeStep);
}
