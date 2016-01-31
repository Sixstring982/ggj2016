package com.lunagameserve;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class TransitionScreen implements Screen {
    private static final int MAX_FRAMES = 100;
    private final Screen fromScreen;
    private final Screen toScreen;

    private int frame = 0;
    private boolean pushed = false;
    private VertexArray array = new VertexArray();

    public TransitionScreen(Screen from, Screen to) {
        this.fromScreen = from;
        this.toScreen = to;
        array.create();
    }

    public UpdatePacket update(long window) {
        frame++;
        if (frame == MAX_FRAMES && !pushed) {
            pushed = true;
            return UpdatePacket.makePush(toScreen);
        } else if (frame > MAX_FRAMES) {
            return UpdatePacket.makePop();
        }

        updateArray();
        return UpdatePacket.makeKeep();
    }

    private void updateArray() {
        float f = (((float)frame / MAX_FRAMES) * 2.0f) - 1.0f;
        array.clear();

        array.add(new Vector3f(-1, -1, 0), new Vector3f(0, 0, 1),
                  new Vector2f(0, 0), 0);
        array.add(new Vector3f(-1, f, 0), new Vector3f(0, 0, 1),
                new Vector2f(1, 0), 0);
        array.add(new Vector3f(1, f, 0), new Vector3f(0, 0, 1),
                  new Vector2f(1, 1), 0);
        array.add(new Vector3f(1, -1, 0), new Vector3f(0, 0, 1),
                new Vector2f(0, 1), 0);

        array.send();
    }

    public void render() {
        fromScreen.render();

        if (frame % 8 < 4) {
            fromScreen.render();
        } else {
            glClearColor(1, 0, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }

        glClearColor(0, 0, 0, 1);
    }
}
