package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;

public class MusicEvent extends CutsceneEvent {
    public MusicEvent(String sound) {
        super();
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        return true;
    }
}
