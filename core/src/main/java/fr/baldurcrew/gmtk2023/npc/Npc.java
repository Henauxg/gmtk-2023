package fr.baldurcrew.gmtk2023.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Npc {
    private float y;
    private float x;
    private Animation<TextureRegion> animation;
    private float animationTimer;
    private NpcAction currentAction;
    private NpcAction previousAction;

    public Npc(float xWorld, float yWorld) {
        this.currentAction = NpcAction.Idle;
        this.previousAction = currentAction;
        this.animation = NpcResources.getInstance().getAnimation(currentAction);
        this.animationTimer = 0f;
        this.x = xWorld;
        this.y = yWorld;
    }

    public void render(float deltaTime, SpriteBatch batch) {
        if (currentAction != previousAction) {
            animation = NpcResources.getInstance().getAnimation(currentAction);
            animationTimer = 0f;
        }
        previousAction = currentAction;

        animationTimer += deltaTime;
        // TODO Not all animations are looping
        final var currentFrame = animation.getKeyFrame(animationTimer, true);

        batch.draw(currentFrame, x, y, NpcResources.NPC_WIDTH, NpcResources.NPC_HEIGHT);
    }

    public void update() {

    }
}
