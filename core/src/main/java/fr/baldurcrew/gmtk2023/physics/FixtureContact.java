package fr.baldurcrew.gmtk2023.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Objects;

public final class FixtureContact {
    private final Fixture handledFixture;
    private final Fixture otherFixture;

    public FixtureContact(Fixture handledFixture, Fixture otherFixture) {
        this.handledFixture = handledFixture;
        this.otherFixture = otherFixture;
    }

    public Fixture handledFixture() {
        return handledFixture;
    }

    public Fixture otherFixture() {
        return otherFixture;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FixtureContact) obj;
        return Objects.equals(this.handledFixture, that.handledFixture) &&
            Objects.equals(this.otherFixture, that.otherFixture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handledFixture, otherFixture);
    }

    @Override
    public String toString() {
        return "FixtureContact[" +
            "handledFixture=" + handledFixture + ", " +
            "otherFixture=" + otherFixture + ']';
    }


}
