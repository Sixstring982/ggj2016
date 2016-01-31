package com.lunagameserve;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GLUtil;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sixstring982 on 1/30/16.
 */
public class BattleScreen implements Screen {
    private ShaderProgram bgProgram = new ShaderProgram();
    private ShaderProgram uiProgram = new ShaderProgram();
    private Texture2D enemyTex;
    private Texture2D selectBox = new Texture2D();
    VertexArray array = new VertexArray();

    int frame = 0;

    public BattleScreen(Texture2D enemyTex) {
        this.enemyTex = enemyTex;
        try {
            selectBox.load(getClass().getResourceAsStream("/textures/attackBox.png"));
            bgProgram.init("/shaders/vertex/2d.vert",
                           "/shaders/fragment/battleBackground.frag");
            uiProgram.init("/shaders/vertex/2d.vert",
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

        enemyTex.setRect(new Vector2f(-0.5f, -0.5f), new Vector2f(1, 1), -0.1f, 0);
        selectBox.setRect(new Vector2f(-0.9f, -0.9f), new Vector2f(1.8f, 0.5f), -0.1f, 1);
    }

    private void bobEnemy() {
        float time = (float)glfwGetTime();
        float amt = (float)Math.sin(time) * 0.1f + 0.4f;

        enemyTex.setRect(new Vector2f(-0.5f, -0.5f + amt), new Vector2f(1, 1), -0.5f, 0);
    }

    public UpdatePacket update(long window) {
        bgProgram.use();
        bgProgram.setVector2(Util.getScreenResolution(window), "iResolution");
        bobEnemy();

        frame++;
        if (frame > 500) {
            return UpdatePacket.makePop();
        }

        return UpdatePacket.makeKeep();
    }

    public void render() {
        bgProgram.use();
        bgProgram.setMatrix4(new Matrix4f(), "mvp");
        bgProgram.setFloat((float) glfwGetTime(), "iGlobalTime");
        array.draw(GL_QUADS, 0, 1, 2, 3);

        uiProgram.use();
        uiProgram.setTextureUnit(enemyTex, "enemySampler");
        uiProgram.setTextureUnit(selectBox, "boxSampler");
        uiProgram.setMatrix4(
                new Matrix4f().rotate((float)Math.sin(glfwGetTime() * 1.2f) * 0.5f, 0, 0, 1), "mvp");
        enemyTex.draw();
        uiProgram.setMatrix4(new Matrix4f(), "mvp");
        selectBox.draw();
    }
}
