package fr.baldurcrew.gmtk2023.level.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import fr.baldurcrew.gmtk2023.level.tiles.types.BlockTile;
import fr.baldurcrew.gmtk2023.level.tiles.types.Tile;
import fr.baldurcrew.gmtk2023.level.tiles.types.TileType;
import fr.baldurcrew.gmtk2023.level.tiles.types.VoidTile;

import java.util.Objects;

public class Tilemap implements Disposable {

    private final int width;
    private final int height;
    private final Vector2 origin;
    private final Vector2 tileSize;
    private final World world;
    private Tile[][] tiles;

    public Tilemap(World world, Vector2 origin, Vector2 tileSize, int width, int height) {
        this.world = world;
        this.origin = origin;
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;

        this.tiles = new Tile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.tiles[i][j] = new VoidTile();
            }
        }
    }

    public void render(float deltaTime, SpriteBatch batch) {
        float renderX = origin.x;
        float renderY = origin.y;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                var tile = tiles[i][j];
                var tileTexture = tile.getTexture();
                if (tileTexture != null) {
                    batch.draw(tileTexture, renderX, renderY, tileSize.x, tileSize.y);
                }
                renderX += tileSize.x;
            }
            renderX = origin.x;
            renderY += tileSize.y;
        }
    }

//    public boolean isValid(TilePosition tile) {
//        return (tile.i() > 0 && tile.i() < width && tile.j() > 0 && tile.j() < height);
//    }

    public boolean isValid(int i, int j) {
        return (i > 0 && i < width && j > 0 && j < height);
    }

    public TilePosition getValidTilePosition(int i, int j) {
        if (!isValid(i, j)) {
            return null;
        }
        return new TilePosition(i, j);
    }

    public TilePosition getValidTilePosition(Vector2 worldPos) {
        int i = Math.round((int) Math.floor((worldPos.x - origin.x) / tileSize.x));
        int j = Math.round((int) Math.floor((worldPos.y - origin.y) / tileSize.y));

        return getValidTilePosition(i, j);
    }

    public TileType setTile(TilePosition tilePosition, TileType tileType) {
        this.tiles[tilePosition.i()][tilePosition.j()].dispose(world);
        this.tiles[tilePosition.i()][tilePosition.j()] = createTile(tileType, tilePosition);
        return tileType;
    }

    private Tile createTile(TileType type, TilePosition tilePosition) {
        switch (type) {
            case Block -> {
                return new BlockTile(world, tilePosition.toWorld(origin, tileSize), tileSize);
            }
            default -> {
                return new VoidTile();
            }
        }
    }

    @Override
    public void dispose() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                tiles[i][j].dispose(world);
            }
        }
    }

//    public TileType getTypeAtPosition(TilePosition tilePos) {
//        if (!isValid(tilePos)) return null;
//        return tiles[tilePos.i()][tilePos.j()].type;
//    }

    public TileType getTypeAtPosition(TilePosition tilePos) {
        return tiles[tilePos.i()][tilePos.j()].type;
    }

    public Vector2 getWorldPosition(TilePosition tilePos) {
        return tilePos.toWorld(origin, tileSize);
    }

    public Vector2 getWorldPosition(int i, int j) {
        return new Vector2(origin.x + i * tileSize.x + tileSize.x / 2f, origin.y + j * tileSize.y + tileSize.y / 2f);
    }

    public boolean isInside(Vector2 worldPos, TileRect rect) {
        final var tilePos = getValidTilePosition(worldPos);
        if (tilePos != null) {
            return rect.contains(tilePos);
        }
        return false;
    }

    // Quick & dirty, apply one texture over every tile of the rect
    public void renderRect(SpriteBatch spriteBatch, Texture texture, TileRect rect) {
        float originX = origin.x + rect.bottomLeft.i * tileSize.x;
        float renderX = originX;
        float renderY = origin.y + rect.bottomLeft.j * tileSize.y;
        for (int j = 0; j < rect.height; j++) {
            for (int i = 0; i < rect.width; i++) {
                spriteBatch.draw(texture, renderX, renderY, tileSize.x, tileSize.y);
                renderX += tileSize.x;
            }
            renderX = originX;
            renderY += tileSize.y;
        }
    }

    public void renderTile(SpriteBatch spriteBatch, Texture texture, TilePosition mouseTile) {
        float renderX = origin.x + mouseTile.i * tileSize.x;
        float renderY = origin.y + mouseTile.j * tileSize.y;
        spriteBatch.draw(texture, renderX, renderY, tileSize.x, tileSize.y);
    }

    public static final class TilePosition {
        private final int i;
        private final int j;

        public TilePosition(int i, int j) {
            this.i = i;
            this.j = j;
        }

        protected Vector2 toWorld(Vector2 origin, Vector2 tileSize) {
            return new Vector2(origin.x + i * tileSize.x + tileSize.x / 2f, origin.y + j * tileSize.y + tileSize.y / 2f);
        }

        @Override
        public String toString() {
            return "TilePosition{" +
                "i=" + i +
                ", j=" + j +
                '}';
        }

        public int i() {
            return i;
        }

        public int j() {
            return j;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (TilePosition) obj;
            return this.i == that.i &&
                this.j == that.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

    }
}
