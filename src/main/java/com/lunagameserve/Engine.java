package com.lunagameserve;

import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class Engine {
    public void start(final long window, Screen top) {
        Stack<Screen> screenStack = new Stack<Screen>();
        screenStack.push(top);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (glfwWindowShouldClose(window) == GLFW_FALSE &&
               screenStack.size() > 0) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            screenStack.peek().update();
            screenStack.peek().render();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
}
