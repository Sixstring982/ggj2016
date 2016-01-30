package com.lunagameserve;

/**
 * Created by sixstring982 on 1/29/16.
 */
public interface Screen {
    UpdatePacket update(final long window);

    void render();
}
