package fr.baldurcrew.gmtk2023;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CommonResources {
    private static CommonResources instance;

    public final BitmapFont defaultFont;
    public final Texture greenTileOverlayTexture;

    public CommonResources() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixeloidSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        defaultFont = generator.generateFont(parameter);
        generator.dispose();

        greenTileOverlayTexture = new Texture("tiles/green_tile_overlay.png");
    }

    public static CommonResources getInstance() {
        if (instance == null) {
            instance = new CommonResources();
        }

        return instance;
    }
}
