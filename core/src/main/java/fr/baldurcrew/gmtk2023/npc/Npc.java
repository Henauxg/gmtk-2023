package fr.baldurcrew.gmtk2023.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.baldurcrew.gmtk2023.inputs.InputType;

public class Npc {
    public static final float NPC_DENSITY = 2f;
    public static final float NPC_FRICTION = 0.25f;
    public static final float NPC_RESTITUTION = 0.2f;
    private static final float MAX_NPC_VELOCITY_X = 2f;

    private final Body body;
    private Animation<TextureRegion> animation;
    private float animationTimer;
    private NpcAnimation currentAnimation;
    private NpcAnimation previousAnimation;

    public Npc(World world, float xWorld, float yWorld) {
        this.currentAnimation = NpcAnimation.Idle;
        this.previousAnimation = currentAnimation;
        this.animation = NpcResources.getInstance().getAnimation(currentAnimation);
        this.animationTimer = 0f;
        this.body = createBody(world, xWorld, yWorld, NPC_DENSITY, NPC_FRICTION, NPC_RESTITUTION);
    }

    private Body createBody(World world, float centerX, float centerY, float density, float friction, float restitution) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(centerX, centerY);
        bodyDef.linearDamping = 0f;
        //bodyDef.angularDamping = 1f;
        bodyDef.fixedRotation = true;

        final Body body = world.createBody(bodyDef);
        body.setUserData(this);
        {
            final PolygonShape colliderPolygon = new PolygonShape();
            final FixtureDef collider = new FixtureDef();

            colliderPolygon.setAsBox(NpcResources.NPC_COLLIDER_WIDTH / 2f, NpcResources.NPC_RENDER_HEIGHT / 2f);

            collider.shape = colliderPolygon;
            collider.density = density;
            collider.friction = friction;
            collider.restitution = restitution;

            body.createFixture(collider);
            colliderPolygon.dispose();
        }

        return body;
    }

    public void render(float deltaTime, SpriteBatch batch) {
        if (currentAnimation != previousAnimation) {
            animation = NpcResources.getInstance().getAnimation(currentAnimation);
            animationTimer = 0f;
        }
        previousAnimation = currentAnimation;

        animationTimer += deltaTime;
        // TODO Not all animations are looping
        final var currentFrame = animation.getKeyFrame(animationTimer, true);

        final float renderX = body.getPosition().x - NpcResources.NPC_RENDER_WIDTH / 2f;
        final float renderY = body.getPosition().y - NpcResources.NPC_RENDER_HEIGHT / 2f;
        batch.draw(currentFrame, renderX, renderY, NpcResources.NPC_RENDER_WIDTH, NpcResources.NPC_RENDER_HEIGHT);
    }

    public void update(InputType input) {
        Vector2 velocity = body.getLinearVelocity();

        float desiredVelocityX = 0f;
        switch (input) {
            case Left -> {
                desiredVelocityX = -MAX_NPC_VELOCITY_X;
            }
            case Right -> {
                desiredVelocityX = MAX_NPC_VELOCITY_X;
            }
            case Idle -> {
                desiredVelocityX = 0;
            }
        }

        float deltaVelX = desiredVelocityX - velocity.x;
        float impulseX = body.getMass() * deltaVelX;

        body.applyLinearImpulse(new Vector2(impulseX, 0f), body.getWorldCenter(), true);
    }
}
