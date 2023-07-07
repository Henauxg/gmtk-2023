package fr.baldurcrew.gmtk2023.npc;

import fr.baldurcrew.gmtk2023.utils.FrameData;

public enum NpcAction {
    Idle(new FrameData(0, 0), new FrameData(1, 0)),
    Blink(new FrameData(0, 1), new FrameData(1, 1)),
    Walk(new FrameData(0, 2), new FrameData(1, 2), new FrameData(2, 2), new FrameData(3, 2)),
    Run(new FrameData(0, 3), new FrameData(1, 3), new FrameData(2, 3), new FrameData(3, 3), new FrameData(4, 3), new FrameData(5, 3), new FrameData(6, 3), new FrameData(7, 3)),
    Duck(new FrameData(0, 4), new FrameData(1, 4), new FrameData(2, 4), new FrameData(3, 4), new FrameData(4, 4), new FrameData(5, 4)),
    Jump(new FrameData(0, 5), new FrameData(1, 5), new FrameData(2, 5), new FrameData(3, 5), new FrameData(4, 5), new FrameData(5, 5), new FrameData(6, 5), new FrameData(7, 5)),
    Disappear(new FrameData(0, 6), new FrameData(1, 6), new FrameData(2, 6)),
    Die(new FrameData(0, 7), new FrameData(1, 7), new FrameData(2, 7), new FrameData(3, 7), new FrameData(4, 7), new FrameData(5, 7), new FrameData(6, 7), new FrameData(7, 7)),
    Attack(new FrameData(0, 8), new FrameData(1, 8), new FrameData(2, 8), new FrameData(3, 8), new FrameData(4, 8), new FrameData(5, 8), new FrameData(6, 8), new FrameData(7, 8));

    private FrameData[] frames;

    NpcAction(FrameData... frames) {
        this.frames = frames;
    }

    public FrameData getFrame(int index) {
        return frames[index];
    }

    public int getFramesCount() {
        return frames.length;
    }

    public FrameData[] getFrames() {
        return frames;
    }
}
