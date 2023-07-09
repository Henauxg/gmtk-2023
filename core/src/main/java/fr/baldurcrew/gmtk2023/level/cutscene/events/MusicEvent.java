package fr.baldurcrew.gmtk2023.level.cutscene.events;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import fr.baldurcrew.gmtk2023.Constants;
import fr.baldurcrew.gmtk2023.level.Level;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;

public class MusicEvent extends CutsceneEvent {
    private final Music music;
    private final Music previousMusic;

    public MusicEvent(Music previousMusic, Music music) {
        super();
        this.previousMusic = previousMusic;
        this.music = music;
    }

    @Override
    public boolean render(SpriteBatch spriteBatch, float deltaTime, Level level) {
        previousMusic.stop();
        music.setLooping(true);
        music.setVolume(Constants.DEFAULT_AUDIO_VOLUME);
        music.play();
        return true;
    }
}
