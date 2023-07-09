package fr.baldurcrew.gmtk2023;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CommonResources {
    private static CommonResources instance;

    public final BitmapFont defaultFont;


    public final Texture greenTileOverlayTexture;
    public final Music mainMenuMusic;
    public final Music mainGameMusic;

    public CommonResources() {
        // Freetype does not appear to work even with gdx-freetype-teavm
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixeloidSans.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 18;
//        defaultFont = generator.generateFont(parameter);
//        generator.dispose();

        defaultFont = new BitmapFont(Gdx.files.internal("fonts/PixeloidSans_36.fnt"));

        greenTileOverlayTexture = new Texture("tiles/green_tile_overlay.png");
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/sinnesloschen-beam.mp3"));
        mainGameMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/chiptune-grooving.mp3"));
    }

    public static CommonResources getInstance() {
        if (instance == null) {
            instance = new CommonResources();
        }

        return instance;
    }
}
