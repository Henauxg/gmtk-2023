package fr.baldurcrew.gmtk2023.level;

import fr.baldurcrew.gmtk2023.CommonResources;
import fr.baldurcrew.gmtk2023.inputs.InputType;
import fr.baldurcrew.gmtk2023.level.cutscene.Cutscene;
import fr.baldurcrew.gmtk2023.level.cutscene.CutsceneEvent;
import fr.baldurcrew.gmtk2023.level.cutscene.events.*;
import fr.baldurcrew.gmtk2023.level.tiles.TileRect;
import fr.baldurcrew.gmtk2023.level.tiles.Tilemap;

import java.util.LinkedList;
import java.util.List;


public class Levels {

    public static final Level LEVEL_1;
    public static final Level LEVEL_2;
    //    public static final Level LEVEL_3;
    public static final Level[] LEVELS;

    private static final float DEFAULT_TIME_PER_LETTER_S = 0.05f;

    static {
        // I am the player now

        // Level 1
        {
            final var startCutsceneEvents = new LinkedList<CutsceneEvent>();
            startCutsceneEvents.add(new NpcMovementEvent(List.of(InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left))); // TODO Give delta manually to level constructor
            startCutsceneEvents.add(new NpcDialogEvent("Okey... I'll get to work on that level.", DEFAULT_TIME_PER_LETTER_S, 0.5f));
            startCutsceneEvents.add(new ImGuiEvent(true));
            startCutsceneEvents.add(new WaitEvent(1.f));
            startCutsceneEvents.add(new NpcDialogEvent("Put the end here, ...", DEFAULT_TIME_PER_LETTER_S, 0.5f));
            startCutsceneEvents.add(new EndAreaEvent(true));
            startCutsceneEvents.add(new NpcDialogEvent("Put some blocks here and here, ...", DEFAULT_TIME_PER_LETTER_S, 0.5f));
            startCutsceneEvents.add(new BlockPlaceEvent(new TileRect(0, 7, 12, 1), 0.25f));
            startCutsceneEvents.add(new NpcDialogEvent("... ...", 0.5f, 0.5f));
            startCutsceneEvents.add(new NpcDialogEvent("Man, I'm so tired of designing those levels.", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            // TODO Think that's easy huh ? Do it .. Player places a block -> Oh yeah ? Okey, then you do it.
            startCutsceneEvents.add(new NpcMovementEvent(List.of(InputType.Left, InputType.Idle, InputType.Right)));
            startCutsceneEvents.add(new NpcDialogEvent("You know what ?", DEFAULT_TIME_PER_LETTER_S, 1f));
            startCutsceneEvents.add(new MusicEvent(CommonResources.getInstance().mainMenuMusic, CommonResources.getInstance().mainGameMusic));
            startCutsceneEvents.add(new NpcMovementEvent(List.of(InputType.Left, InputType.Right)));
            startCutsceneEvents.add(new NpcDialogEvent("For once, I'll play and ... ... Yes ! You'll build the level !", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            startCutsceneEvents.add(new NpcDialogEvent("I am the player now !", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            startCutsceneEvents.add(new InputQueueEvent(true));

            final var levelBlocks = new LinkedList<Tilemap.TilePosition>();
            for (int i = 20; i < 30; i++) {
                levelBlocks.add(new Tilemap.TilePosition(i, 7));
            }

            final var levelInputs = new LinkedList<InputType>();
            levelInputs.addAll(List.of(InputType.Idle, InputType.Idle, InputType.Idle, InputType.Left, InputType.Idle, InputType.Left, InputType.Idle, InputType.Idle, InputType.Right, InputType.Left, InputType.Idle, InputType.Left, InputType.Idle, InputType.JumpLeft, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Idle, InputType.Left, InputType.Idle, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left));

            LEVEL_1 = new Level("Level 1", 23, 8, 29, 8, levelBlocks, new Cutscene(startCutsceneEvents), new TileRect(8, 8, 1, 2), levelInputs);
        }

        // Level 2
        {
//            final var startCutsceneEvents = new LinkedList<CutsceneEvent>();
//            startCutsceneEvents.add(new NpcMovementEvent(List.of(InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left, InputType.Left))); // TODO Give delta manually to level constructor
//            startCutsceneEvents.add(new WaitEvent(1.f));
//
//            final var levelBlocks = new LinkedList<Tilemap.TilePosition>();
//            for (int i = 1; i < 28; i++) {
//                levelBlocks.add(new Tilemap.TilePosition(i, 7));
//            }
//
//            final var levelInputs = new LinkedList<InputType>();
//            levelInputs.addAll(List.of(InputType.Idle, InputType.Idle, InputType.Idle, InputType.Left, InputType.Idle, InputType.Left, InputType.Idle, InputType.JumpLeft, InputType.Idle, InputType.Right, InputType.Left, InputType.Idle, InputType.Left, InputType.Idle, InputType.JumpLeft));
//
//            LEVEL_2 = new Level("Level 2", 12, 5, 12, 5, levelBlocks, new Cutscene(startCutsceneEvents), new TileRect(4, 2, 5, 3), levelInputs);
        }

        // Level 2
        {
            final var startCutsceneEvents = new LinkedList<CutsceneEvent>();
            startCutsceneEvents.add(new MusicEvent(CommonResources.getInstance().mainGameMusic, CommonResources.getInstance().mainMenuMusic));
            startCutsceneEvents.add(new WaitEvent(1.f));
            startCutsceneEvents.add(new NpcDialogEvent("Well done.", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            startCutsceneEvents.add(new NpcMovementEvent(List.of(InputType.Right, InputType.Right)));
            startCutsceneEvents.add(new BlockPlaceEvent(new TileRect(14, 5, 1, 1), 0.25f));
            startCutsceneEvents.add(new BlockPlaceEvent(new TileRect(16, 5, 1, 1), 0.25f));
            startCutsceneEvents.add(new BlockPlaceEvent(new TileRect(13, 3, 1, 1), 0.25f));
            startCutsceneEvents.add(new BlockPlaceEvent(new TileRect(17, 3, 1, 1), 0.25f));
            startCutsceneEvents.add(new BlockPlaceEvent(new TileRect(14, 2, 3, 1), 0.25f));
            startCutsceneEvents.add(new NpcDialogEvent("But the end of the jam is near.", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            startCutsceneEvents.add(new NpcDialogEvent("And this game is nowhere near finished.", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            startCutsceneEvents.add(new NpcDialogEvent("So I'll let you deal with my random inputs !", DEFAULT_TIME_PER_LETTER_S, 1.5f));
            startCutsceneEvents.add(new MusicEvent(CommonResources.getInstance().mainMenuMusic, CommonResources.getInstance().mainGameMusic));

            // TODO Add blocks + rmeove blocks

            // TOOD Remove blocks
            // TODO Prevent random inputs during cutscene
            // TODO reticule ffs


            final var levelBlocks = new LinkedList<Tilemap.TilePosition>();

            levelBlocks.add(new Tilemap.TilePosition(20, 7));
//            levelBlocks.add(new Tilemap.TilePosition(21, 7));
            levelBlocks.add(new Tilemap.TilePosition(23, 7));
//            levelBlocks.add(new Tilemap.TilePosition(24, 7));

            for (int i = 8; i < 12; i++) {
                levelBlocks.add(new Tilemap.TilePosition(i, 7));
            }

            LEVEL_2 = new Level("Level 2", 11, 8, 8, 8, levelBlocks, new Cutscene(startCutsceneEvents));
        }

        LEVELS = new Level[]{LEVEL_1, LEVEL_2};
    }

}
