package com.lunagameserve;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class VertexArray {
    private static int vaoId = -1;
    private int id;
    private boolean ready = false;
    private List<Vector3f> verts = new ArrayList<Vector3f>();
    private int remoteSize;

    public void create() {
        if (vaoId == -1) {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
        }
        id = glGenBuffers();
    }

    public void clear() {
        verts.clear();
    }

    public boolean isReady() {
        return ready;
    }

    public void add(Vector3f v) {
        verts.add(v);
    }

    public void send() {
        remoteSize = verts.size();
        FloatBuffer buf = BufferUtils.createFloatBuffer(remoteSize * 3);
        for (Vector3f v : verts) {
            buf.put(v.x);
            buf.put(v.y);
            buf.put(v.z);
        }
        buf.flip();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        ready = true;
    }

    public void draw(final int mode, final int attribId) {
        if (!isReady()) {
            throw new RuntimeException("VertexArray not ready.");
        }
        glEnableVertexAttribArray(attribId);
        glBindBuffer(GL_ARRAY_BUFFER, id);

        glVertexAttribPointer(attribId, 3, GL_FLOAT, false, 0, 0);
        glDrawArrays(mode, 0, remoteSize);

        glDisableVertexAttribArray(attribId);
    }
}
