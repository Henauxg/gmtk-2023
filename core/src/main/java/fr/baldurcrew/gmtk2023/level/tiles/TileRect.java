package fr.baldurcrew.gmtk2023.level.tiles;

public class TileRect {
    public Tilemap.TilePosition bottomLeft;
    public int width;
    public int height;

    public TileRect(int i, int j, int w, int h) {
        this.bottomLeft = new Tilemap.TilePosition(i, j);
        this.width = w;
        this.height = h;
    }

    public boolean contains(Tilemap.TilePosition tilePos) {
        return (tilePos.i() >= bottomLeft.i() && tilePos.i() <= bottomLeft.i() + width && tilePos.j() >= bottomLeft.j() && tilePos.j() <= bottomLeft.j() + height);
    }
}
