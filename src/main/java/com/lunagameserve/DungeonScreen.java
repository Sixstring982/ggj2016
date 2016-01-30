package com.lunagameserve;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class DungeonScreen implements Screen {
    private World world = new World();
    private Camera camera = new Camera();
    private ShaderProgram program = new ShaderProgram();

    public DungeonScreen() {
        try {
            world.load();
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

        camera.update(window, new Predicate() {
            public boolean eval() {
                return world.isInside(camera.getEye()) ||
                       world.isInside(camera.getFeet());
            }
        });
        if (world.isInside(camera.getEye())) {
            camera.undoMove();
        }
        return UpdatePacket.makeKeep();
    }

    public void render() {
        Matrix4f mvp = camera.generateMVP();
        program.use();
        program.setMatrix4(mvp, "mvp");
        program.setFloat((float)glfwGetTime(), "iGlobalTime");
        program.setVector3(camera.getEye(), "eye");

        world.render();
    }
}
