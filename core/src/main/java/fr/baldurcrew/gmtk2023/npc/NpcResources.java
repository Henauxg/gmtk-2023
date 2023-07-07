package fr.baldurcrew.gmtk2023.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.baldurcrew.gmtk2023.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class NpcResources {

    public static final float NPC_WIDTH = 1;
    public static final float NPC_HEIGHT = 1;

    private static final float FRAME_DURATION = 0.25f;
    private static final int NPC_SPRITE_SHEET_COL_COUNT = 8;
    private static final int NPC_SPRITE_SHEET_ROW_COUNT = 9;

    private static NpcResources instance;

    private final Map<NpcAction, Animation<TextureRegion>> npcAnimations;

    public NpcResources() {
        npcAnimations = new HashMap<>();
        final var spriteSheet = Utils.loadAndSplitSpriteSheet("npc/npc_spritesheet.png", NPC_SPRITE_SHEET_COL_COUNT, NPC_SPRITE_SHEET_ROW_COUNT);
        Stream.of(NpcAction.values()).forEach(action -> {
            Animation<TextureRegion> animation = Utils.buildAnimation(FRAME_DURATION, action.getFrames(), spriteSheet);
            npcAnimations.put(action, animation);
        });
    }

    public static NpcResources getInstance() {
        if (instance == null) {
            instance = new NpcResources();
        }

        return instance;
    }

    public Animation<TextureRegion> getAnimation(NpcAction action) {
        return npcAnimations.get(action);
    }
}
