package fr.baldurcrew.gmtk2023.level.cutscene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;

public abstract class CutsceneEvent {

    protected boolean initialized;

    public abstract boolean render(SpriteBatch spriteBatch, float deltaTime, Level level);
}
