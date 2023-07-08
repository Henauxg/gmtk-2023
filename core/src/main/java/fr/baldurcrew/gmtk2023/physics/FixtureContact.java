package fr.baldurcrew.gmtk2023.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

public record FixtureContact(Fixture handledFixture, Fixture otherFixture) {

}
