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
    private VertexArray array = new VertexArray();

    public void load(String path) throws IOException {
        voxels.clear();
        NBTInputStream in = new NBTInputStream(
                getClass().getResourceAsStream(path));

        CompoundTag root = (CompoundTag)in.readTag();
        Map<String, Tag> map = root.getValue();
        short width = ((ShortTag) map.get("Width")).getValue();
        short height = ((ShortTag) map.get("Height")).getValue();
        short length = ((ShortTag) map.get("Length")).getValue();

        byte[] bytes = ((ByteArrayTag) map.get("Blocks")).getValue();

        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    if (bytes[(y * length + z) * width + x] != 0) {
                        voxels.add(new Voxel(new Vector3f(x, y, z)));
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
}
