package fr.baldurcrew.gmtk2023.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParallaxLayer {

    private final TextureRegion textureRegion;
    private final Texture texture;
    private final float wiggleAmplitude;
    private final double horizontalScrollLoopDuration;
    private final double verticalScrollLoopDuration;

    private float textureAspectRatio;
    private float heightScalingRatio;
    private float yOffsetPositionFactor;
    private boolean isWrapHorizontally, iswWrapVertically;
    private float wiggleSpeed;
    private double animationTimerX;
    private double animationTimerY;
//    private int textureRegionOffsetX;
//    private int textureRegionOffsetY;

    public ParallaxLayer(Texture texture, double horizontalScrollLoopDuration, double verticalScrollLoopDuration, float heightScalingRatio, float yOffsetPositionFactor, boolean isWrapHorizontally, boolean iswWrapVertically, float wiggleSpeed, float wiggleAmplitude) {
        this.texture = texture;
        this.horizontalScrollLoopDuration = horizontalScrollLoopDuration;
        this.verticalScrollLoopDuration = verticalScrollLoopDuration;
        this.isWrapHorizontally = isWrapHorizontally;
        this.iswWrapVertically = iswWrapVertically;
        this.wiggleSpeed = wiggleSpeed;
        this.wiggleAmplitude = wiggleAmplitude;
        this.heightScalingRatio = heightScalingRatio;
        this.yOffsetPositionFactor = yOffsetPositionFactor;
        this.texture.setWrap(
            this.isWrapHorizontally ? Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge,
            this.iswWrapVertically ? Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge
        );
        textureRegion = new TextureRegion(texture);
        textureAspectRatio = texture.getWidth() / texture.getHeight();
    }

    public void render(Camera camera, SpriteBatch spriteBatch, float deltaTime) {
        animationTimerX += deltaTime;
        animationTimerY += deltaTime;
        if (animationTimerX >= horizontalScrollLoopDuration) {
            animationTimerX = 0;
        }
        final var percentX =(float) (animationTimerX / horizontalScrollLoopDuration) ;
        textureRegion.setU(percentX);
        textureRegion.setRegionWidth(texture.getWidth());

        if (iswWrapVertically) {
            if (animationTimerY >= verticalScrollLoopDuration) {
                animationTimerY = 0;
            }
            textureRegion.setV((float) (animationTimerY / verticalScrollLoopDuration));
            textureRegion.setRegionHeight(texture.getHeight());
        }
        var yOffset = camera.viewportHeight * yOffsetPositionFactor;

        if (wiggleSpeed > 0) {
            yOffset += (float) Math.cos(2 * Math.PI * percentX * wiggleSpeed) * wiggleAmplitude;
        }

        final var layerHeight = camera.viewportWidth / textureAspectRatio * heightScalingRatio;
        spriteBatch.draw(textureRegion, 0, camera.viewportHeight - layerHeight + yOffset, camera.viewportWidth, layerHeight);
    }
}
