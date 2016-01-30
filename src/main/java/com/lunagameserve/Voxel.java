package com.lunagameserve;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Voxel {
    private static final float VOXEL_SIZE = 1.0f;
    private static final float VOXEL_MARGIN = 0.01f;

    private Vector3f center;
    private Vector3f offset = new Vector3f(0, 0, 0);

    public Voxel(Vector3f center) {
        this.center = new Vector3f(center);
    }

    public void appendTo(VertexArray v) {
        float h = VOXEL_SIZE - VOXEL_MARGIN;
        Vector3f center = new Vector3f(this.center).add(offset);
        Vector3f I = new Vector3f(1, 0, 0);
        Vector3f J = new Vector3f(0, 1, 0);
        Vector3f K = new Vector3f(0, 0, 1);
        Vector3f nI = new Vector3f(-1, 0, 0);
        Vector3f nJ = new Vector3f(0, -1, 0);
        Vector3f nK = new Vector3f(0, 0, -1);

        Vector2f c1 = new Vector2f(0, 0);
        Vector2f c2 = new Vector2f(1, 0);
        Vector2f c3 = new Vector2f(1, 1);
        Vector2f c4 = new Vector2f(0, 1);

        /* append bottom */
        v.add(new Vector3f(center.x, center.y, center.z), nJ, c1);
        v.add(new Vector3f(center.x + h, center.y, center.z), nJ, c2);
        v.add(new Vector3f(center.x + h, center.y, center.z + h), nJ, c3);
        v.add(new Vector3f(center.x, center.y, center.z + h), nJ, c4);

        /* append edge 1 */
        v.add(new Vector3f(center.x, center.y, center.z), nK, c1);
        v.add(new Vector3f(center.x, center.y + h, center.z), nK, c2);
        v.add(new Vector3f(center.x + h, center.y + h, center.z), nK, c3);
        v.add(new Vector3f(center.x + h, center.y, center.z), nK, c4);

        /* append edge 2 */
        v.add(new Vector3f(center.x, center.y, center.z), nI, c1);
        v.add(new Vector3f(center.x, center.y, center.z + h), nI, c2);
        v.add(new Vector3f(center.x, center.y + h, center.z + h), nI, c3);
        v.add(new Vector3f(center.x, center.y + h, center.z), nI, c4);

        /* append edge 3 */
        v.add(new Vector3f(center.x + h, center.y, center.z), I, c1);
        v.add(new Vector3f(center.x + h, center.y + h, center.z), I, c2);
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h), I, c3);
        v.add(new Vector3f(center.x + h, center.y, center.z + h), I, c4);

        /* append edge 4 */
        v.add(new Vector3f(center.x, center.y, center.z + h), K, c1);
        v.add(new Vector3f(center.x + h, center.y, center.z + h), K, c2);
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h), K, c3);
        v.add(new Vector3f(center.x, center.y + h, center.z + h), K, c4);

        /* append top */
        v.add(new Vector3f(center.x, center.y + h, center.z), J, c1);
        v.add(new Vector3f(center.x, center.y + h, center.z + h), J, c2);
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h), J, c3);
        v.add(new Vector3f(center.x + h, center.y + h, center.z), J, c4);
    }

    public Vector3f getAbsolutePosition() {
        return new Vector3f(offset).add(center);
    }

    public void applyOffset(Vector3f offset) {
        this.offset = new Vector3f(offset);
    }
}
