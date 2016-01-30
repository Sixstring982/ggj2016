package com.lunagameserve;

import com.sun.prism.ps.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GLUtil;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class DungeonScreen implements Screen {
    private Voxel[] voxels = new Voxel[16];
    private Camera camera = new Camera();
    private ShaderProgram program = new ShaderProgram();

    public DungeonScreen() {
        for (int i = 0; i < voxels.length; i++) {
            voxels[i] = new Voxel(new Vector3f(i, i, i));
        }
        try {
            program.init("/shaders/vertex/default.vert",
                         "/shaders/fragment/default.frag");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdatePacket update(final long window) {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == 1) {
            return UpdatePacket.makePop();
        }

        camera.update(window);
        return UpdatePacket.makeKeep();
    }

    public void render() {
        Matrix4f mvp = camera.generateMVP();
        program.use();
        program.setMatrix4(mvp, "mvp");
        for (Voxel v : voxels) {
            v.render();
        }

        glBegin(GL_LINES);
        for (int x = -10; x <= 10; x++) {
            for (int y = -10; y <= 10; y++) {
                glVertex3f(x, 0, y);
                glVertex3f(x + 1, 0, y);

                glVertex3f(x + 1, 0, y);
                glVertex3f(x + 1, 0, y + 1);

                glVertex3f(x + 1, 0, y + 1);
                glVertex3f(x, 0, y + 1);

                glVertex3f(x, 0, y + 1);
                glVertex3f(x, 0, y);
            }
        }
        glEnd();
    }
}
