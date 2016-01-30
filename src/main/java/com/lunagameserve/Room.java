package com.lunagameserve;

import org.jnbt.*;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class Room {
    private List<Voxel> voxels = new ArrayList<Voxel>();
    private int width;
    private int height;
    private int length;
    private Vector3f offset = new Vector3f(0, 0, 0);
    boolean[][][] collisionMap = null;
    private VertexArray array = new VertexArray();
    private Set<Direction> exits = new HashSet<Direction>();
    private Set<Direction> hookups = new HashSet<Direction>();

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

        /* Set up exit flags */
        if (!collisionMap[7][1][0]) {
            exits.add(Direction.NORTH);
        }
        if (!collisionMap[0][1][7]) {
            exits.add(Direction.EAST);
        }
        if (!collisionMap[7][1][15]) {
            exits.add(Direction.SOUTH);
        }
        if (!collisionMap[15][1][7]) {
            exits.add(Direction.WEST);
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
        array.draw(GL_QUADS, 0, 1, 2);
    }

    public boolean isInside(Vector3f vec) {
        vec = new Vector3f(vec).sub(offset);
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
        return vec.x >= 0.5f && vec.x < width - 0.5f &&
               vec.y >= 0.5f && vec.y < height - 0.5f &&
               vec.z >= 0.5f && vec.z < length - 0.5f;
    }

    public void setOffset(Vector3i offset) {
        this.offset.x = offset.x;
        this.offset.y = offset.y;
        this.offset.z = offset.z;

        array.clear();
        for (Voxel v : voxels) {
            v.applyOffset(this.offset);
            v.appendTo(array);
        }
        array.send();
    }

    public boolean containsWithOffset(Vector3f eye) {
        return isInBounds(new Vector3f(eye).sub(offset));
    }

    public Set<Direction> getExits() {
        return new HashSet<Direction>(exits);
    }

    public void hookup(Direction dir) {
        hookups.add(dir);
    }

    public boolean isHooked(Direction dir) {
        return hookups.contains(dir);
    }

    public Vector3i getBoundary(Direction dir) {
        switch (dir) {
            case NORTH: return new Vector3i(0, 0, -(length - 1));
            case EAST:  return new Vector3i(-(width - 1), 0, 0);
            case SOUTH: return new Vector3i(0, 0, length - 1);
            case WEST:  return new Vector3i(width - 1, 0, 0);
            default: throw new IllegalArgumentException();
        }
    }

    public Vector3i getOffset() {
        return new Vector3i(
                (int)offset.x, (int)offset.y, (int)offset.z
        );
    }

    public void addVisibleRooms(VertexArray visibleBlocks,
                                Vector3f eye, float distance) {
        /* Is the eye close? */
        if (new Vector3f(offset).add(width / 2.0f, 0.0f, length / 2.0f)
                .sub(eye).length() < length + distance) {
            for (Voxel v : voxels) {
                if (v.getAbsolutePosition().sub(eye).length() < distance) {
                    v.appendTo(visibleBlocks);
                }
            }
        }
    }
}
