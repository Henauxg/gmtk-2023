package fr.baldurcrew.gmtk2023.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import fr.baldurcrew.gmtk2023.inputs.InputSequencer;
import fr.baldurcrew.gmtk2023.physics.ContactHandler;
import fr.baldurcrew.gmtk2023.physics.FixtureContact;

import java.util.HashSet;
import java.util.Set;

public class Npc implements ContactHandler, Disposable {
    public static final float NPC_DENSITY = 2f;
    public static final float NPC_FRICTION = 0.25f;
    public static final float NPC_RESTITUTION = 0.2f;
    private static final float MAX_NPC_VELOCITY_X = 2f;
    private static final float JUMP_IMPULSE_Y = 5.5f;
    private final Body body;
    private final World world;
    private Animation<TextureRegion> animation;
    private float animationTimer;
    private NpcAnimation currentAnimation;
    private NpcAnimation previousAnimation;
    private int previousInputId;
    private Set<Fixture> contactGroundFixtures;
    private boolean touchingGround;
    private boolean shouldFlipX;

    public Npc(World world, Vector2 position) {
        this.world = world;
        this.currentAnimation = NpcAnimation.Idle;
        this.previousInputId = 0;
        this.previousAnimation = currentAnimation;
        this.animation = NpcResources.getInstance().getAnimation(currentAnimation);
        this.animationTimer = 0f;
        this.body = createBody(world, position, NPC_DENSITY, NPC_FRICTION, NPC_RESTITUTION);
        this.contactGroundFixtures = new HashSet<>();
        this.touchingGround = false;
        this.shouldFlipX = false;
    }

    private Body createBody(World world, Vector2 position, float density, float friction, float restitution) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
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
        {
            final PolygonShape footSensorPolygon = new PolygonShape();
            final FixtureDef footSensor = new FixtureDef();

            final float footSensorHeight = NpcResources.NPC_RENDER_HEIGHT / 10f;
            footSensorPolygon.setAsBox(0.9f * NpcResources.NPC_COLLIDER_WIDTH / 2f, footSensorHeight, new Vector2(0, -NpcResources.NPC_RENDER_HEIGHT / 2f - footSensorHeight / 2f), 0f);

            footSensor.shape = footSensorPolygon;
            footSensor.isSensor = true;

            Fixture footSensorsFixture = body.createFixture(footSensor);
            // TODO May need user data
//            footSensorsFixture.setUserData(CharacterSensors.Foot);
            footSensorPolygon.dispose();
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
        if ((shouldFlipX && !currentFrame.isFlipX() || (!shouldFlipX && currentFrame.isFlipX()))) {
            currentFrame.flip(true, false);
        }

        final float renderX = body.getPosition().x - NpcResources.NPC_RENDER_WIDTH / 2f;
        final float renderY = body.getPosition().y - NpcResources.NPC_RENDER_HEIGHT / 2f;
        batch.draw(currentFrame, renderX, renderY, NpcResources.NPC_RENDER_WIDTH, NpcResources.NPC_RENDER_HEIGHT);
    }

    public void update(InputSequencer.Input input) {
        touchingGround = !contactGroundFixtures.isEmpty();

        Vector2 velocity = body.getLinearVelocity();

        float desiredVelocityX = 0f;
        float impulseY = 0f;
        switch (input.type()) {
            case Left -> {
                desiredVelocityX = -MAX_NPC_VELOCITY_X;
                currentAnimation = NpcAnimation.Run;
                shouldFlipX = true;
            }
            case Right -> {
                desiredVelocityX = MAX_NPC_VELOCITY_X;
                currentAnimation = NpcAnimation.Run;
                shouldFlipX = false;
            }
            case JumpLeft -> {
                if (previousInputId != input.id()) {
                    impulseY = JUMP_IMPULSE_Y;
                }
                desiredVelocityX = -MAX_NPC_VELOCITY_X / 2f;
                currentAnimation = NpcAnimation.Jump;
                shouldFlipX = true;
            }
            case JumpRight -> {
                if (previousInputId != input.id()) {
                    impulseY = JUMP_IMPULSE_Y;
                }
                desiredVelocityX = MAX_NPC_VELOCITY_X / 2f;
                currentAnimation = NpcAnimation.Jump;
                shouldFlipX = false;
            }
            case Idle -> {
                desiredVelocityX = 0;
                currentAnimation = NpcAnimation.Idle;
            }
        }
        previousInputId = input.id();

        float deltaVelX = desiredVelocityX - velocity.x;
        float impulseX = body.getMass() * deltaVelX;

        // If falling, still allow to jump
        if (impulseY > 0 && velocity.y < 0) {
            impulseY -= velocity.y;
        }

        body.applyLinearImpulse(new Vector2(impulseX, impulseY), body.getWorldCenter(), true);
    }

    public boolean isTouchingGround() {
        return touchingGround;
    }

    @Override
    public void handleContactBegin(FixtureContact contact) {
        // TODO test for user data. Simple for now because there are not many things in the physic world
        if (contact.handledFixture().isSensor()) {
            contactGroundFixtures.add(contact.otherFixture());
        }
    }

    @Override
    public void handleContactEnd(FixtureContact contact) {
        if (contact.handledFixture().isSensor()) {
            contactGroundFixtures.remove(contact.otherFixture());
        }
    }

    @Override
    public void handlePreSolve(Contact contact, FixtureContact fixtures) {

    }

    @Override
    public void dispose() {
        world.destroyBody(body);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
