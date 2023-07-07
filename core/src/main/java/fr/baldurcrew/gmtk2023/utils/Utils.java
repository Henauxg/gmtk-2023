package fr.baldurcrew.gmtk2023.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utils {
    public static TextureRegion[][] loadAndSplitSpriteSheet(String imagePath, int columns, int rows) {
        Texture charactersSheet = new Texture(Gdx.files.internal(imagePath));
        TextureRegion[][] frames = TextureRegion.split(charactersSheet, charactersSheet.getWidth() / columns, charactersSheet.getHeight() / rows);
        return frames;
    }

    public static Animation<TextureRegion> buildAnimation(float frameDuration, FrameData[] frames, TextureRegion[][] spriteSheet) {
        TextureRegion[] animationFrames = new TextureRegion[frames.length];
        for (int i = 0; i < frames.length; i++) {
            final var frameData = frames[i];
            animationFrames[i] = spriteSheet[frameData.j][frameData.i];
        }
        return new Animation<TextureRegion>(frameDuration, animationFrames);
    }
}
