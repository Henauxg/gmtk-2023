package fr.baldurcrew.gmtk2023.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.TimeUnit;

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

    public static Vector2 getInputWorldPosition(float viewportWidth, float viewportHeight) {
        float xViewportPercent = (float) Gdx.input.getX() / (float) Gdx.graphics.getWidth();
        float yViewportPercent = 1f - (float) Gdx.input.getY() / (float) Gdx.graphics.getHeight();
        return new Vector2(xViewportPercent * viewportWidth, yViewportPercent * viewportHeight);
    }

    public static String secondsToDisplayString(float inputSeconds) {
//        final String minutes = String.format("%02i",(int) Math.round(Math.floor(inputSeconds % 3600 / 60)));
//        final String seconds = String.format("%02d", Math.round(Math.floor(inputSeconds % 60)));
//
//        if (inputSeconds < 3600) return minutes + ':' + seconds;
//
//        final String hours = String.format("%02d", Math.round(Math.floor(inputSeconds / 3600)));
//        return hours + ':' + minutes + ':' + seconds;

        // Quick & dirty horror, for web compat
        long secsAsLong = (long) inputSeconds * 1000;
        long hours = TimeUnit.MILLISECONDS
            .toHours(secsAsLong);
        secsAsLong -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS
            .toMinutes(secsAsLong);
        secsAsLong -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS
            .toSeconds(secsAsLong);

        if (inputSeconds < 3600)
            return (minutes < 10 ? "0" : "") + String.valueOf(minutes) + ':' + (seconds < 10 ? "0" : "") + String.valueOf(seconds);
        else
            return String.valueOf(hours) + ':' + (minutes < 10 ? "0" : "") + String.valueOf(minutes) + ':' + (seconds < 10 ? "0" : "") + String.valueOf(seconds);
    }

}
