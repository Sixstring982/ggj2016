package com.lunagameserve;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class Util {
    public static Vector2f getScreenResolution(final long window) {
        ByteBuffer w = BufferUtils.createByteBuffer(4);
        ByteBuffer h = BufferUtils.createByteBuffer(4);
        glfwGetWindowSize(window, w, h);
        int width = w.getInt(0);
        int height = h.getInt(0);
        return new Vector2f(width, height);
    }
}
