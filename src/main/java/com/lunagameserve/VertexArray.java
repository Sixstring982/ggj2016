package com.lunagameserve;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class VertexArray {
    private static int vaoId = -1;
    private int vertId;
    private int normId;
    private boolean ready = false;
    private List<Vector3f> verts = new ArrayList<Vector3f>();
    private List<Vector3f> normals = new ArrayList<Vector3f>();
    private int remoteVerts;
    private int remoteNormals;

    public void create() {
        if (vaoId == -1) {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
        }
        vertId = glGenBuffers();
        normId = glGenBuffers();
    }

    public void clear() {
        verts.clear();
        normals.clear();
    }

    public boolean isReady() {
        return ready;
    }

    public void add(Vector3f v, Vector3f n) {
        verts.add(v);
        normals.add(n);
    }

    public void send() {
        remoteVerts = verts.size();
        FloatBuffer buf = BufferUtils.createFloatBuffer(remoteVerts * 3);
        for (Vector3f v : verts) {
            buf.put(v.x);
            buf.put(v.y);
            buf.put(v.z);
        }
        buf.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vertId);
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);

        remoteNormals = normals.size();
        buf = BufferUtils.createFloatBuffer(remoteNormals * 3);
        for (Vector3f n : normals) {
            buf.put(n.x);
            buf.put(n.y);
            buf.put(n.z);
        }
        buf.flip();
        glBindBuffer(GL_ARRAY_BUFFER, normId);
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        ready = true;
    }

    public void draw(final int mode,
                     final int vertId,
                     final int normId) {
        if (!isReady()) {
            throw new RuntimeException("VertexArray not ready.");
        }
        glEnableVertexAttribArray(vertId);
        glBindBuffer(GL_ARRAY_BUFFER, this.vertId);
        glVertexAttribPointer(vertId, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(normId);
        glBindBuffer(GL_ARRAY_BUFFER, this.normId);
        glVertexAttribPointer(normId, 3, GL_FLOAT, false, 0, 0);

        glDrawArrays(mode, 0, remoteVerts);

        glDisableVertexAttribArray(normId);
        glDisableVertexAttribArray(vertId);
    }
}
