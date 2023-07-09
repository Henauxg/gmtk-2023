package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;

public class InputQueueEvent extends CutsceneEvent {
    private final boolean show;

    public InputQueueEvent(boolean show) {
        super();
        this.show = true;
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        level.showInputQueue();
        return true;
    }
}
