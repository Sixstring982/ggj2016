package com.lunagameserve;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class BattleScreen implements Screen {
    public UpdatePacket update(long window) {
        return UpdatePacket.makePop();
    }

    public void render() {

    }
}
