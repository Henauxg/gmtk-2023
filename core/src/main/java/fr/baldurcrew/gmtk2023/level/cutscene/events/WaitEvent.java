package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;

public class WaitEvent extends CutsceneEvent {

    private float waitDuration;
    private float waitTimer;

    public WaitEvent(float waitDuration) {
        super();
        this.waitDuration = waitDuration;
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        waitTimer += deltaTime;
        return waitTimer >= waitDuration;
    }
}
