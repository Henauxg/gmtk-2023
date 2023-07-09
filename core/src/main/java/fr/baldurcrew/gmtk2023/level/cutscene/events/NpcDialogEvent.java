package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import fr.baldurcrew.gmtk2023.CommonResources;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;
import fr.baldurcrew.gmtk2023.npc.NpcResources;

public class NpcDialogEvent extends CutsceneEvent {
    private final String text;
    private final float eventDuration;
    private final Sound soundEffect;
    private final Label label;
    private final float waitDuration;
    private final Label.LabelStyle labelStyle;
    private float waitTimer;
    private float textTimer;
    private int lettersDisplayed;

    public NpcDialogEvent(String text, float timePerLetter, float waitDuration) {
        super();
        this.text = text;
        this.eventDuration = text.length() * timePerLetter;
        this.soundEffect = NpcResources.getInstance().npcSpeakingSound;
        this.waitDuration = waitDuration;

        labelStyle = new Label.LabelStyle();
        labelStyle.font = CommonResources.getInstance().defaultFont;
        label = new Label("", labelStyle);
        label.setSize(Gdx.graphics.getWidth() / 2f, 2f);
        label.setPosition(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() * 3f / 4f);
        label.setAlignment(Align.center);
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        if (!initialized) {
            level.getStage().addActor(label);
            initialized = true;
        }
        var lettersToDisplay = Math.min(Math.round(text.length() * textTimer / eventDuration), text.length() - 1);
        if (lettersToDisplay > lettersDisplayed) {
            if (text.charAt(lettersToDisplay) != ' ' && text.charAt(lettersToDisplay) != '\'' && text.charAt(lettersToDisplay) != '.' && text.charAt(lettersToDisplay) != ',') {
                soundEffect.play(Constants.DEFAULT_AUDIO_VOLUME);
            }
            lettersDisplayed = lettersToDisplay;
            label.setText(text.substring(0, lettersToDisplay + 1));
        }

        textTimer += deltaTime;
        if (textTimer >= eventDuration) {
            waitTimer += deltaTime;
            if (waitTimer >= waitDuration) {
                label.remove();
                return true;
            }
        }
        return false;
    }
}
