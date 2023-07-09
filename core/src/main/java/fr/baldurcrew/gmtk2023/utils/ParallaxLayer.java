package fr.baldurcrew.gmtk2023.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParallaxLayer {

    private final TextureRegion textureRegion;
    private final Texture texture;

    private final double horizontalScrollLoopDuration;
    private float textureAspectRatio;
    private float heightScalingRatio;
    private float yOffsetPositionFactor;
    private boolean isWrapHorizontally;
    private double animationTimerX;

    public ParallaxLayer(Texture texture, double horizontalScrollLoopDuration, float heightScalingRatio, float yOffsetPositionFactor, boolean isWrapHorizontally) {
        this.texture = texture;
        this.horizontalScrollLoopDuration = horizontalScrollLoopDuration;
        this.isWrapHorizontally = isWrapHorizontally;
        this.heightScalingRatio = heightScalingRatio;
        this.yOffsetPositionFactor = yOffsetPositionFactor;
        this.texture.setWrap(
            this.isWrapHorizontally ? Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge,
            Texture.TextureWrap.ClampToEdge
        );
        textureRegion = new TextureRegion(texture);
        textureAspectRatio = texture.getWidth() / texture.getHeight();
    }

    public void render(Camera camera, SpriteBatch spriteBatch, float deltaTime) {
        animationTimerX += deltaTime;
        if (animationTimerX >= horizontalScrollLoopDuration) {
            animationTimerX = 0;
        }
        final var percentX = (float) (animationTimerX / horizontalScrollLoopDuration);
        textureRegion.setU(percentX);
        textureRegion.setRegionWidth(texture.getWidth());

        var yOffset = camera.viewportHeight * yOffsetPositionFactor;
        final var layerHeight = camera.viewportWidth / textureAspectRatio * heightScalingRatio;
        spriteBatch.draw(textureRegion, 0, camera.viewportHeight - layerHeight + yOffset, camera.viewportWidth, layerHeight); // - layerHeight + yOffset
    }
}
