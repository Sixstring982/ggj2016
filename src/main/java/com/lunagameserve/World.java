package com.lunagameserve;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class World {
    private static final int WORLD_SIZE = 10;
    private List<Room> rooms = new ArrayList<Room>();
    private Random rand = new Random(System.nanoTime());
    private VertexArray visibleBlocks = new VertexArray();
    private Set<Vector3i> takenOffsets = new HashSet<Vector3i>();
    private int enemyRoom = 0;

    void load() throws IOException {
        String[] validRooms = new String[13];
        for (int i = 0; i < validRooms.length; i++) {
            validRooms[i] = "room_" + (i + 1);
        }

        Room r = genRoom(validRooms[0], new Vector3i(0, 0, 0));
        rooms.add(r);
        hookupExits(r, validRooms);
        visibleBlocks.create();

        /* Pick one of the rooms to hold an enemy */
        enemyRoom = rand.nextInt(rooms.size());
    }

    private void hookupExits(Room room, String[] roomFiles) throws IOException {
        if (rooms.size() >= WORLD_SIZE) {
            return ;
        }
        List<Direction> shuffledExits =
                new ArrayList<Direction>(room.getExits());
        Collections.shuffle(shuffledExits, rand);

        for (Direction e : shuffledExits) {
            if (!room.isHooked(e)) {
                Vector3i offset = new Vector3i(room.getOffset())
                                              .add(room.getBoundary(e));
                /* Don't want two rooms in the same place */
                if (takenOffsets.contains(offset)) {
                    continue;
                }
                /* Try a few times to hook up a room here */
                for (int i = 0; i < 5; i++) {
                /* Load a new room here and hook up its exits */
                    Room c = genRoom(roomFiles[rand.nextInt(roomFiles.length)],
                            offset);
                    if (c.getExits().contains(e.opposite()) &&
                       !c.isHooked(e.opposite())) {
                        room.hookup(e);
                        c.hookup(e.opposite());
                        rooms.add(c);
                        hookupExits(c, roomFiles);
                        takenOffsets.add(offset);
                        break;
                    }
                }
            }
        }
    }

    public void updateRenderTargets(Vector3f eye, float distance) {
        visibleBlocks.clear();
        for (Room r : rooms) {
            r.addVisibleRooms(visibleBlocks, eye, distance);
        }
        visibleBlocks.send();
    }

    private Room genRoom(String filename, Vector3i offset) throws IOException {
        Room room = new Room();
        room.load("/models/" + filename + ".schematic");
        room.send();
        room.setOffset(offset);
        return room;
    }

    void render() {
        if (visibleBlocks.isReady()) {
            visibleBlocks.draw(GL_QUADS, 0, 1, 2, 3);
        } else {
            for (Room r : rooms) {
                r.render();
            }
        }
    }

    public boolean isInside(Vector3f eye) {
        for (Room r : rooms) {
            if (r.containsWithOffset(eye)) {
                if (r.isInside(eye)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInEnemyRoom(Vector3f eye) {
        return rooms.get(enemyRoom).containsWithOffset(eye);
    }

    public Vector3f getEnemyPos() {
        Vector3i v = rooms.get(enemyRoom).getOffset().add(8, 2, 8);
        return new Vector3f(v.x, v.y, v.z);
    }
}
