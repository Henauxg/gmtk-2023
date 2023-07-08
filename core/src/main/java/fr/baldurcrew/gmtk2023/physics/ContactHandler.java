package fr.baldurcrew.gmtk2023.physics;

import com.badlogic.gdx.physics.box2d.Contact;

public interface ContactHandler {
    void handleContactBegin(FixtureContact contact);

    void handleContactEnd(FixtureContact contact);

    void handlePreSolve(Contact contact, FixtureContact fixtures);
}
