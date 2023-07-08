package fr.baldurcrew.gmtk2023.level.tiles.types;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Tile {

    public final TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    public Texture getTexture() {
        return null;
    }

    public void dispose(World world) {
    }
}
