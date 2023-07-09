package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;
import fr.baldurcrew.gmtk2023.level.tiles.TileRect;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;

import java.util.LinkedList;

public class BlockPlaceEvent extends CutsceneEvent {

    private final float delay;
    private float placeTimer;
    private LinkedList<Tilemap.TilePosition> blocksToPlace;

    public BlockPlaceEvent(TileRect tileRect, float delay) {
        super();
        this.delay = delay;

        blocksToPlace = new LinkedList<>();
        for (int j = tileRect.bottomLeft.j(); j < tileRect.bottomLeft.j() + tileRect.height; j++) {
            for (int i = tileRect.bottomLeft.i(); i < tileRect.bottomLeft.i() + tileRect.width; i++) {
                blocksToPlace.add(new Tilemap.TilePosition(i, j));
            }
        }
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        if (blocksToPlace.size() == 0) {
            return true;
        }

        placeTimer += deltaTime;
        if (placeTimer >= delay) {
            var block = blocksToPlace.removeFirst();
            level.addBlock(block);
            placeTimer = 0f;
        }
        return false;
    }
}
