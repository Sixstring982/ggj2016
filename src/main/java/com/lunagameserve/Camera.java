package com.lunagameserve;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Camera {
    private final float MOVE_SPEED = 0.1f;
    private final float TURN_SPEED = 0.03f;
    private final float PLAYER_HEIGHT = 1.6f;
    private Vector3f position = new Vector3f(5.0f, PLAYER_HEIGHT + 0.5f, 5.0f);
    private Vector3f oldPosition = new Vector3f(position);
    private Vector3f target = new Vector3f(1.0f, 0.0f, 0.0f);

    private float theta = (float)Math.PI / 2.0f;
    private float phi = 0.0f;
    private static final float TARGET_RADIUS = 1.0f;

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Matrix4f generateMVP() {
        Vector3f posTarget = new Vector3f();
        position.add(target, posTarget);
        return new Matrix4f()
                .perspective(1.5f, 1.0f, 0.1f, 100.0f)
                .lookAt(position, posTarget, new Vector3f(0.0f, 1.0f, 0.0f));
    }

    public void update(final long window, Predicate collisionPred) {
        float oldTheta = theta;
        oldPosition = new Vector3f(position);
        if (glfwGetKey(window, GLFW_KEY_A) == 1) {
            theta += TURN_SPEED;
        }

        if (glfwGetKey(window, GLFW_KEY_D) == 1) {
            theta -= TURN_SPEED;
        }

        if (glfwGetKey(window, GLFW_KEY_W) == 1) {
            position.x += MOVE_SPEED * target.x;
            if (collisionPred.eval()) {
                position.x -= MOVE_SPEED * target.x;
            }

            position.z += MOVE_SPEED * target.z;
            if (collisionPred.eval()) {
                position.z -= MOVE_SPEED * target.z;
            }
        }

        if (glfwGetKey(window, GLFW_KEY_S) == 1) {
            position.x -= MOVE_SPEED * target.x;
            if (collisionPred.eval()) {
                position.x += MOVE_SPEED * target.x;
            }

            position.z -= MOVE_SPEED * target.z;
            if (collisionPred.eval()) {
                position.z += MOVE_SPEED * target.z;
            }
        }

        if (theta != oldTheta) {
            updateTarget();
        }
    }

    public void undoMove() {
        this.position = new Vector3f(oldPosition);
    }

    private void updateTarget() {
        float x = (float)(TARGET_RADIUS * Math.sin(theta) * Math.sin(phi));
        float y = (float)(TARGET_RADIUS * Math.cos(theta));
        float z = (float)(TARGET_RADIUS * Math.sin(theta) * Math.cos(phi));
        target = new Vector3f(z, x, y);
    }

    public Vector3f getEye() {
        return position;
    }

    public Vector3f getFeet() {
        return new Vector3f(position).sub(0.0f, 0.2f, 0.0f);
    }
}
