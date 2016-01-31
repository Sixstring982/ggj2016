package com.lunagameserve;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class BattleScreen implements Screen {
    private ShaderProgram program = new ShaderProgram();
    VertexArray array = new VertexArray();

    public BattleScreen() {
        try {
            program.init("/shaders/vertex/2d.vert",
                         "/shaders/fragment/2d.frag");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        array.create();

        array.add(new Vector3f(-1, -1, 0), new Vector3f(0, 0, 1),
                new Vector2f(0, 0), 0);
        array.add(new Vector3f(1, -1, 0), new Vector3f(0, 0, 1),
                new Vector2f(0, 1), 0);
        array.add(new Vector3f(1, 1, 0), new Vector3f(0, 0, 1),
                new Vector2f(1, 1), 0);
        array.add(new Vector3f(-1, 1, 0), new Vector3f(0, 0, 1),
                new Vector2f(1, 0), 0);

        array.send();
    }

    public UpdatePacket update(long window) {
        program.use();
        program.setVector2(Util.getScreenResolution(window), "iResolution");
        return UpdatePacket.makeKeep();
    }

    public void render() {
        program.use();
        program.setFloat((float)glfwGetTime(), "iGlobalTime");
        array.draw(GL_QUADS, 0, 1, 2, 3);
    }
}
