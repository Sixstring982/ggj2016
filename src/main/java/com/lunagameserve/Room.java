package com.lunagameserve;

import org.jnbt.*;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class Room {
    private List<Voxel> voxels = new ArrayList<Voxel>();
    private int width;
    private int height;
    private int length;
    boolean[][][] collisionMap = null;
    private VertexArray array = new VertexArray();

    public void load(String path) throws IOException {
        voxels.clear();
        NBTInputStream in = new NBTInputStream(
                getClass().getResourceAsStream(path));

        CompoundTag root = (CompoundTag)in.readTag();
        Map<String, Tag> map = root.getValue();
        this.width = ((ShortTag) map.get("Width")).getValue();
        this.height = ((ShortTag) map.get("Height")).getValue();
        this.length = ((ShortTag) map.get("Length")).getValue();

        this.collisionMap = new boolean[width][height][length];

        byte[] bytes = ((ByteArrayTag) map.get("Blocks")).getValue();

        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    if (bytes[(y * length + z) * width + x] != 0) {
                        voxels.add(new Voxel(new Vector3f(x, y, z)));
                        collisionMap[x][y][z] = true;
                    } else {
                        collisionMap[x][y][z] = false;
                    }
                }
            }
        }
        send();
    }

    public void send() {
        array.create();
        for (Voxel v : voxels) {
            v.appendTo(array);
        }
        array.send();
    }

    public void render() {
        array.draw(GL_QUADS, 0, 1);
    }

    public boolean isInside(Vector3f vec) {
        return collisionMap != null &&
               isInBounds(vec) &&
                (collisionMap[(int)(vec.x)][(int)(vec.y)][(int)(vec.z)] ||
                 collisionMap[(int)(vec.x - 0.5f)][(int)(vec.y)][(int)(vec.z)] ||
                 collisionMap[(int)(vec.x + 0.5f)][(int)(vec.y)][(int)(vec.z)] ||
                 collisionMap[(int)(vec.x)][(int)(vec.y - 0.5f)][(int)(vec.z)] ||
                 collisionMap[(int)(vec.x)][(int)(vec.y + 0.5f)][(int)(vec.z)] ||
                 collisionMap[(int)(vec.x)][(int)(vec.y)][(int)(vec.z - 0.5f)] ||
                 collisionMap[(int)(vec.x)][(int)(vec.y)][(int)(vec.z + 0.5f)]);
    }

    private boolean isInBounds(Vector3f vec) {
        return vec.x >= 0 && vec.x < width &&
               vec.y >= 0 && vec.y < height &&
               vec.z >= 0 && vec.z < length;
    }
}
