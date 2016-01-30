package com.lunagameserve;

import org.joml.Vector3f;
import org.lwjgl.opengl.GLUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class Room {
    private List<Voxel> voxels = new ArrayList<Voxel>();
    private VertexArray array = new VertexArray();

    public void init() {
        array.create();
        for (int i = 0; i < 10; i++) {
            voxels.add(new Voxel(new Vector3f(i, i, i)));
            voxels.get(i).appendTo(array);
        }
        array.send();
    }

    public void render() {
        array.draw(GL_QUADS, 0, 1);
    }
}
