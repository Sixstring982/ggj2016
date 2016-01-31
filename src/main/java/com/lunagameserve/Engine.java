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

        glEnable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (glfwWindowShouldClose(window) == GLFW_FALSE &&
               screenStack.size() > 0) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            UpdatePacket packet = screenStack.peek().update(window);
            if (packet.getResult() == UpdateResult.POP) {
                screenStack.pop();
            } else if (packet.getResult() == UpdateResult.PUSH) {
                screenStack.push(packet.getScreen());
            } else {
                screenStack.peek().render();
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
}
