package fr.baldurcrew.gmtk2023.level.cutscene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.events.BlockPlaceEvent;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;

import java.util.LinkedList;
import java.util.List;

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

    // No time anymore
    public List<Tilemap.TilePosition> collectBlockPlacements() {
        final var tilePositions = new LinkedList<Tilemap.TilePosition>();
        for (CutsceneEvent event : this.events) {
            if (event instanceof BlockPlaceEvent) {
                tilePositions.addAll(((BlockPlaceEvent) event).getBlocks());
            }
        }

        return tilePositions;
    }
}
