package com.lunagameserve;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Camera {
    private final float MOVE_SPEED = 0.1f;
    private final float TURN_SPEED = 0.1f;
    private Vector3f position = new Vector3f(0.0f, 1.0f, 5.0f);
    private Vector3f target = new Vector3f(0.0f, 0.0f, 0.0f);

    private float theta = 0.0f;
    private float rho = 0.0f;

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Matrix4f generateMVP() {
        return new Matrix4f()
                .perspective(1.5f, 1.0f, 0.1f, 100.0f)
                .lookAt(position, target, new Vector3f(0.0f, 1.0f, 0.0f));
    }

    public void update(final long window) {
        if (glfwGetKey(window, GLFW_KEY_A) == 1) {
            theta += TURN_SPEED;
        }

        if (glfwGetKey(window, GLFW_KEY_D) == 1) {
            theta -= TURN_SPEED;
        }

        position.x = (float)Math.sin(theta) * 3.0f;
        position.z = (float)Math.cos(theta) * 3.0f;

        if (glfwGetKey(window, GLFW_KEY_SPACE) == 1) {
            position.y += MOVE_SPEED;
        }

        if (glfwGetKey(window, GLFW_KEY_C) == 1) {
            position.y -= MOVE_SPEED;
        }
    }

    public Vector3f getEye() {
        return position;
    }
}
