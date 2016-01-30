package com.lunagameserve;

import org.joml.Vector3f;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Voxel {
    private static final float VOXEL_SIZE = 1.0f;
    private static final float VOXEL_MARGIN = 0.05f;
    private static final float VOXEL_HALF = VOXEL_SIZE / 2.0f;

    private Vector3f center;

    public Voxel(Vector3f center) {
        this.center = new Vector3f(center);
    }

    public void appendTo(VertexArray v) {
        float h = VOXEL_HALF - VOXEL_MARGIN;
        Vector3f I = new Vector3f(1, 0, 0);
        Vector3f J = new Vector3f(0, 1, 0);
        Vector3f K = new Vector3f(0, 0, 1);
        Vector3f nI = new Vector3f(-1, 0, 0);
        Vector3f nJ = new Vector3f(0, -1, 0);
        Vector3f nK = new Vector3f(0, 0, -1);

        /* append bottom */
        v.add(new Vector3f(center.x - h, center.y - h, center.z - h), nJ);
        v.add(new Vector3f(center.x + h, center.y - h, center.z - h), nJ);
        v.add(new Vector3f(center.x + h, center.y - h, center.z + h), nJ);
        v.add(new Vector3f(center.x - h, center.y - h, center.z + h), nJ);

        /* append edge 1 */
        v.add(new Vector3f(center.x - h, center.y - h, center.z - h), nK);
        v.add(new Vector3f(center.x - h, center.y + h, center.z - h), nK);
        v.add(new Vector3f(center.x + h, center.y + h, center.z - h), nK);
        v.add(new Vector3f(center.x + h, center.y - h, center.z - h), nK);

        /* append edge 2 */
        v.add(new Vector3f(center.x - h, center.y - h, center.z - h), nI);
        v.add(new Vector3f(center.x - h, center.y - h, center.z + h), nI);
        v.add(new Vector3f(center.x - h, center.y + h, center.z + h), nI);
        v.add(new Vector3f(center.x - h, center.y + h, center.z - h), nI);

        /* append edge 3 */
        v.add(new Vector3f(center.x + h, center.y - h, center.z - h), I);
        v.add(new Vector3f(center.x + h, center.y + h, center.z - h), I);
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h), I);
        v.add(new Vector3f(center.x + h, center.y - h, center.z + h), I);

        /* append edge 4 */
        v.add(new Vector3f(center.x - h, center.y - h, center.z + h), K);
        v.add(new Vector3f(center.x + h, center.y - h, center.z + h), K);
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h), K);
        v.add(new Vector3f(center.x - h, center.y + h, center.z + h), K);

        /* append top */
        v.add(new Vector3f(center.x - h, center.y + h, center.z - h), J);
        v.add(new Vector3f(center.x - h, center.y + h, center.z + h), J);
        v.add(new Vector3f(center.x + h, center.y + h, center.z + h), J);
        v.add(new Vector3f(center.x + h, center.y + h, center.z - h), J);
    }
}
