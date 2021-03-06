package com.lunagameserve;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Texture2D {
    private int id;
    private int unitId;
    private static int nextUnitId = 0;
    private boolean ready = true;
    private VertexArray array = new VertexArray();
    public void bind() {
        if (!ready) {
            throw new IllegalStateException("Texture not ready.");
        }
        glActiveTexture(GL_TEXTURE0 + unitId);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void load(InputStream stream) throws IOException {
        PNGDecoder dec = new PNGDecoder(stream);
        ByteBuffer buf = ByteBuffer
                          .allocateDirect(4 * dec.getWidth() * dec.getHeight());
        dec.decode(buf, dec.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, dec.getWidth(), dec.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        stream.close();

        unitId = nextUnitId++;
        array.create();
        ready = true;
    }

    public void setRect(Vector2f pos, Vector2f size, float depth, int texture) {
        array.clear();
        array.add(new Vector3f(pos.x, pos.y, depth), new Vector3f(0, 0, 1),
                new Vector2f(1, 1), texture);
        array.add(new Vector3f(pos.x + size.x, pos.y, depth), new Vector3f(0, 0, 1),
                new Vector2f(0, 1), texture);
        array.add(new Vector3f(pos.x + size.x, pos.y + size.y, depth), new Vector3f(0, 0, 1),
                new Vector2f(0, 0), texture);
        array.add(new Vector3f(pos.x, pos.y + size.y, depth), new Vector3f(0, 0, 1),
                new Vector2f(1, 0), texture);
        array.send();
    }

    public void draw() {
        array.draw(GL_QUADS, 0, 1, 2, 3);
    }

    public void unload() {
        if (!ready) {
            throw new IllegalStateException("Texture not ready");
        }
        glDeleteTextures(id);
    }

    public int getUnit() {
        return unitId;
    }
}
