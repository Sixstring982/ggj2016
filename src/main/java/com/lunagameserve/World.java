package com.lunagameserve;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class World {
    private static final int WORLD_SIZE = 10;
    private List<Room> rooms = new ArrayList<Room>();
    private Random rand = new Random(System.nanoTime());

    void load() throws IOException {
        String[] validRooms = new String[]{
                "template"
        };

        Room r = addRoom(validRooms[0], new Vector3i(0, 0, 0));
        hookupExits(r, validRooms);
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
                /* Load a new room here and hook up its exits */
                Room c = addRoom(roomFiles[rand.nextInt(roomFiles.length)],
                        new Vector3i(room.getOffset()).add(room.getBoundary(e)));
                room.hookup(e);
                c.hookup(e.opposite());
                hookupExits(c, roomFiles);
            }
        }
    }

    private Room addRoom(String filename, Vector3i offset) throws IOException {
        Room room = new Room();
        room.load("/models/" + filename + ".schematic");
        room.send();
        room.setOffset(offset);
        rooms.add(room);
        return room;
    }

    void render() {
        for (Room r : rooms) {
            r.render();
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
}
