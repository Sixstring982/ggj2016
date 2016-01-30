package com.lunagameserve;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class GLScreen implements Screen {
    public UpdatePacket update() {
        return UpdatePacket.makeKeep();
    }

    public void render() {
        glColor3f(0.0f, 1.0f, 0.0f);
        glBegin(GL_QUADS); {
            glVertex3f(-1.0f, -1.0f, 0.0f);
            glVertex3f(-1.0f, 1.0f, 0.0f);
            glVertex3f(1.0f, 1.0f, 0.0f);
            glVertex3f(1.0f, -1.0f, 0.0f);
        } glEnd();
    }
}
