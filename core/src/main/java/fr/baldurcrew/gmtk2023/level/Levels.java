package fr.baldurcrew.gmtk2023.level;

import fr.baldurcrew.gmtk2023.level.tiles.TileRect;

public class Levels {

    public static final Level LEVEL_1 = new Level(20, 8, new TileRect(4, 2, 5, 3));
    public static final Level LEVEL_2 = new Level(12, 5, new TileRect(4, 2, 5, 3));
    public static final Level[] LEVELS = new Level[]{LEVEL_1, LEVEL_2};

}
