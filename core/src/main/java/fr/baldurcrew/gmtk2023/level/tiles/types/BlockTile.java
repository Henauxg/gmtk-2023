package fr.baldurcrew.gmtk2023.level.tiles.types;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BlockTile extends Tile {

    public static final float DEFAULT_BLOCK_DENSITY = 2f;
    public static final float DEFAULT_BLOCK_FRICTION = 0.25f;
    public static final float DEFAULT_BLOCK_RESTITUTION = 0.2f;

    private static final Texture tileTexture = new Texture("tile.png");
    private final Body body;


    public BlockTile(World world, Vector2 position, Vector2 size) {
        super(TileType.Block);
        this.body = createBody(world, position, size);
    }

    private Body createBody(World world, Vector2 position, Vector2 size) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);

        final Body body = world.createBody(bodyDef);
        body.setUserData(this);
        {
            final PolygonShape colliderPolygon = new PolygonShape();
            final FixtureDef collider = new FixtureDef();

            colliderPolygon.setAsBox(size.x / 2f, size.y / 2f);

            collider.shape = colliderPolygon;
            collider.density = DEFAULT_BLOCK_DENSITY;
            collider.friction = DEFAULT_BLOCK_FRICTION;
            collider.restitution = DEFAULT_BLOCK_RESTITUTION;

            body.createFixture(collider);
            colliderPolygon.dispose();
        }

        return body;
    }

    @Override
    public Texture getTexture() {
        return tileTexture;
    }

    @Override
    public void dispose(World world) {
        world.destroyBody(body);
    }
}
