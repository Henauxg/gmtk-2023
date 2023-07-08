package fr.baldurcrew.gmtk2023;

import com.badlogic.gdx.math.Vector2;

public class Constants {
    public static final float MIN_TIME_STEP = 0.25f;
    public static final float TIME_STEP = 1 / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    public static final int GRAVITY_VALUE = -10;
    public static final float VIEWPORT_HEIGHT = 16;
    public static final float VIEWPORT_WIDTH = 16f / 9f * VIEWPORT_HEIGHT;
    public static final Vector2 TILE_SIZE = new Vector2(1f, 1f);
}
