package com.lunagameserve;

import org.joml.Vector2f;
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
    private int texId;
    private boolean ready = false;
    private List<Vector3f> verts = new ArrayList<Vector3f>();
    private List<Vector3f> normals = new ArrayList<Vector3f>();
    private List<Vector2f> texs = new ArrayList<Vector2f>();
    private int remoteVerts;
    private int remoteNormals;
    private int remoteTexs;

    public void create() {
        if (vaoId == -1) {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
        }
        vertId = glGenBuffers();
        normId = glGenBuffers();
        texId = glGenBuffers();
    }

    public void clear() {
        verts.clear();
        normals.clear();
    }

    public boolean isReady() {
        return ready;
    }

    public void add(Vector3f v, Vector3f n, Vector2f t) {
        verts.add(v);
        normals.add(n);
        texs.add(t);
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

        remoteTexs = texs.size();
        buf = BufferUtils.createFloatBuffer(remoteTexs * 3);
        for (Vector2f t : texs) {
            buf.put(t.x);
            buf.put(t.y);
        }
        buf.flip();
        glBindBuffer(GL_ARRAY_BUFFER, texId);
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);

        GLUtil.checkGLError();
        ready = true;
    }

    public void draw(final int mode,
                     final int vertId,
                     final int normId,
                     final int texId) {
        if (!isReady()) {
            throw new RuntimeException("VertexArray not ready.");
        }
        glEnableVertexAttribArray(vertId);
        glBindBuffer(GL_ARRAY_BUFFER, this.vertId);
        glVertexAttribPointer(vertId, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(normId);
        glBindBuffer(GL_ARRAY_BUFFER, this.normId);
        glVertexAttribPointer(normId, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(texId);
        glBindBuffer(GL_ARRAY_BUFFER, this.texId);
        glVertexAttribPointer(texId, 2, GL_FLOAT, false, 0, 0);

        glDrawArrays(mode, 0, remoteVerts);

        glDisableVertexAttribArray(texId);
        glDisableVertexAttribArray(normId);
        glDisableVertexAttribArray(vertId);
    }
}
