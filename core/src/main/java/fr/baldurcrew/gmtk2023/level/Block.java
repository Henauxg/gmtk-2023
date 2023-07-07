package fr.baldurcrew.gmtk2023.level;

import com.badlogic.gdx.physics.box2d.*;

public class Block {

    public static final float DEFAULT_BLOCK_DENSITY = 2f;
    public static final float DEFAULT_BLOCK_FRICTION = 0.25f;
    public static final float DEFAULT_BLOCK_RESTITUTION = 0.2f;

    private final Body body;

    public Block(World world, float centerX, float centerY, float width, float height) {
        this.body = createBody(world, centerX, centerY, width, height);
    }

    private Body createBody(World world, float centerX, float centerY, float width, float height) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centerX, centerY);

        final Body body = world.createBody(bodyDef);
        body.setUserData(this);
        {
            final PolygonShape colliderPolygon = new PolygonShape();
            final FixtureDef collider = new FixtureDef();

            colliderPolygon.setAsBox(width / 2f, height / 2f);

            collider.shape = colliderPolygon;
            collider.density = DEFAULT_BLOCK_DENSITY;
            collider.friction = DEFAULT_BLOCK_FRICTION;
            collider.restitution = DEFAULT_BLOCK_RESTITUTION;

            body.createFixture(collider);
            colliderPolygon.dispose();
        }

        return body;
    }
}
