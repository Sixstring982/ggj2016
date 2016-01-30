package com.lunagameserve;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Voxel {
    private static final float VOXEL_SIZE = 1.0f;
    private static final float VOXEL_HALF = VOXEL_SIZE / 2.0f;

    private Vector3f center;
    private VertexArray array;

    public Voxel(Vector3f center) {
        this.center = new Vector3f(center);
        array = buildArray();
    }

    public void render() {
        array.draw(GL_QUADS, 0);
    }

    private VertexArray buildArray() {
        float h = VOXEL_HALF;
        VertexArray v = new VertexArray();
        v.create();
        v.clear();

        /* append top */
        v.add(new Vector3f(center.x - h, center.y - h, center.z - h));
        v.add(new Vector3f(center.x + h, center.y - h, center.z - h));
        v.add(new Vector3f(center.x + h, center.y - h, center.z + h));
        v.add(new Vector3f(center.x - h, center.y - h, center.z + h));

        /* append edge 1 */
        v.add(new Vector3f(center.x - h, center.y - h, center.z - h));
        v.add(new Vector3f(center.x - h, center.y + h, center.z - h));
        v.add(new Vector3f(center.x + h, center.y + h, center.z - h));
        v.add(new Vector3f(center.x + h, center.y - h, center.z - h));

        /* append edge 2 */
        v.add(new Vector3f(center.x - h, center.y - h, center.z - h));
        v.add(new Vector3f(center.x - h, center.y - h, center.z + h));
        v.add(new Vector3f(center.x - h, center.y + h, center.z + h));
        v.add(new Vector3f(center.x - h, center.y + h, center.z - h));

        /* append edge 3 */
        v.add(new Vector3f(center.x + h, center.y - h, center.z - h));
        v.add(new Vector3f(center.x + h, center.y + h, center.z - h));
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h));
        v.add(new Vector3f(center.x + h, center.y - h, center.z + h));

        /* append edge 4 */
        v.add(new Vector3f(center.x - h, center.y - h, center.z + h));
        v.add(new Vector3f(center.x + h, center.y - h, center.z + h));
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h));
        v.add(new Vector3f(center.x - h, center.y + h, center.z + h));

        /* append bottom */
        v.add(new Vector3f(center.x - h, center.y + h, center.z - h));
        v.add(new Vector3f(center.x - h, center.y + h, center.z + h));
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h));
        v.add(new Vector3f(center.x + h, center.y + h, center.z - h));

        v.send();
        return v;
    }
}
