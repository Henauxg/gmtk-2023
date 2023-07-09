package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.inputs.InputType;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;

import java.util.List;

public class NpcMovementEvent extends CutsceneEvent {
    private final List<InputType> inputs;
    private float waitDuration;
    private float waitTimer;

    public NpcMovementEvent(List<InputType> inputs) {
        this.inputs = inputs;
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        if (!initialized) {
            level.addInputs(inputs);
            waitDuration = 0;
            // TODO Could do better
            inputs.forEach(input -> waitDuration += input.ticks * Constants.TIME_STEP);
            initialized = true;
        } else {
            waitTimer += deltaTime;
        }
        return waitTimer >= waitDuration;
    }
}
