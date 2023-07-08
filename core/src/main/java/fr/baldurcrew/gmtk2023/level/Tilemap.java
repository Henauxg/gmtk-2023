package fr.baldurcrew.gmtk2023.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tilemap {

    private final int width;
    private final int height;
    private final Vector2 origin;
    private final Vector2 tileSize;

    private TileType[][] tiles;
    private Texture tileTexture;

    public Tilemap(Vector2 origin, Vector2 tileSize, int width, int height) {
        this.origin = origin;
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;

        this.tiles = new TileType[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.tiles[i][j] = TileType.Void;
            }
        }

        tileTexture = new Texture("tile.png");
    }

    public void render(float deltaTime, SpriteBatch batch) {
        float renderX = origin.x;
        float renderY = origin.y;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                final var tile = tiles[i][j];
                switch (tile) {
                    case Void -> {
                    }
                    case Block -> {
                        batch.draw(tileTexture, renderX, renderY, tileSize.x, tileSize.y);
                    }
                }
                renderX += tileSize.x;
            }
            renderX = origin.x;
            renderY += tileSize.y;
        }
    }

    public boolean isValid(TilePosition tile) {
        return (tile.i() > 0 && tile.i() < width && tile.j() > 0 && tile.j() < height);
    }

    public boolean isValid(int i, int j) {
        return (i > 0 && i < width && j > 0 && j < height);
    }

    public TilePosition getValidTilePosition(int i, int j) {
        if (!isValid(i, j)) {
            return null;
        }
        return new TilePosition(i, j);
    }

    public TilePosition getValidTilePosition(float xWorld, float yWorld) {
        int i = Math.round((int) Math.floor((xWorld - origin.x) / tileSize.x));
        int j = Math.round((int) Math.floor((yWorld - origin.y) / tileSize.y));

        return getValidTilePosition(i, j);
    }

    public TileType setTile(TilePosition tile, TileType tileType) {
        if (!isValid(tile)) {
            Gdx.app.log(this.getClass().getName(), "Invalid setTile parameters: " + tile.i() + " " + tile.j());
            return null;
        }
        this.tiles[tile.i()][tile.j()] = tileType;
        return tileType;
    }

    public record TilePosition(int i, int j) {
    }
}
