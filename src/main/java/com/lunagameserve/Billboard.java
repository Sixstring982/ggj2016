package com.lunagameserve;

import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class Billboard {
    private Texture2D tex;
    private Vector3f pos;
    private VertexArray array = new VertexArray();

    public void init(Texture2D tex, Vector3f pos) {
        this.tex = tex;
        this.pos = new Vector3f(pos);
        array.create();
    }

    public void setPos(Vector3f pos) {
        this.pos = new Vector3f(pos);
    }

    public Vector3f getPos() {
        return pos;
    }

    public void updateFacing(Vector3f normal) {
        array.clear();
        /* Find orthogonal vector */
        Vector3f ortho = new Vector3f(normal).cross(0, 1, 0).normalize();

        array.add(new Vector3f(pos).add(ortho).sub(0, 1, 0), normal,
                  new Vector2f(0, 1), 1);
        array.add(new Vector3f(pos).sub(ortho).sub(0, 1, 0), normal,
                  new Vector2f(1, 1), 1);
        array.add(new Vector3f(pos).sub(ortho).add(0, 1, 0), normal,
                  new Vector2f(1, 0), 1);
        array.add(new Vector3f(pos).add(ortho).add(0, 1, 0), normal,
                  new Vector2f(0, 0), 1);
        array.send();
    }

    public void render() {
        array.draw(GL_QUADS, 0, 1, 2, 3);
    }
}
