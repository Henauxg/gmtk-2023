package fr.baldurcrew.gmtk2023.level.cutscene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;

import java.util.LinkedList;

public class Cutscene {

    private final LinkedList<CutsceneEvent> events;
    private CutsceneEvent currentEvent;

    public Cutscene(LinkedList<CutsceneEvent> events) {
        this.events = events;
        if (events.size() > 0) {
            currentEvent = events.removeFirst();
        }
    }

    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        if (currentEvent.render(spriteBatch, deltaTime, level)) {
            currentEvent = (events.size() > 0) ? events.removeFirst() : null;
        }

        return currentEvent == null;
    }
}
