package com.lunagameserve;

/**
 * Created by sixstring982 on 1/29/16.
 */
public final class UpdatePacket {
    private final UpdateResult result;

    private final Screen screen;

    private UpdatePacket(UpdateResult result,
                         Screen screen) {
        this.result = result;
        this.screen = screen;
    }

    public static UpdatePacket makePush(Screen screen) {
        return new UpdatePacket(UpdateResult.PUSH, screen);
    }

    public static UpdatePacket makePop() {
        return new UpdatePacket(UpdateResult.POP, null);
    }

    public static UpdatePacket makeKeep() {
        return new UpdatePacket(UpdateResult.KEEP, null);
    }
}
