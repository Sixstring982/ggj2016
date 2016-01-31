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
    private Texture2D texture = new Texture2D();
    private Texture2D enemyTex = new Texture2D();
    private Billboard enemy = new Billboard();
    private boolean hideNextFrame = false;

    public DungeonScreen() {
        try {
            world.load();
            enemy.setPos(world.getEnemyPos());
            texture.load(getClass().getResourceAsStream("/textures/cobble.png"));
            enemyTex.load(getClass().getResourceAsStream("/textures/enemy.png"));
            enemy.init(enemyTex, new Vector3f(10, 2, 10));

            texture.bind();
            enemyTex.bind();
            program.init("/shaders/vertex/default.vert",
                         "/shaders/fragment/default.frag");
            program.use();
            program.setTextureUnit(texture, "sampler");
            program.setTextureUnit(enemyTex, "enemySampler");
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

        enemy.updateFacing(
                new Vector3f(camera.getEye())
                        .sub(enemy.getPos()).normalize());
        world.updateRenderTargets(camera.getEye(), 5.0f);
        if (world.isInside(camera.getEye())) {
            camera.undoMove();
        }
        enemy.setPos(world.getEnemyPos());

        if (hideNextFrame) {
            hideNextFrame = false;
            world.moveEnemy();
        }

        if (camera.getEye().sub(enemy.getPos()).length() < 3.0f) {
            UpdatePacket p = UpdatePacket.makePush(new TransitionScreen(this,
                    new BattleScreen()));
            hideNextFrame = true;
            return p;
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

        if (world.isInEnemyRoom(camera.getEye())) {
            enemy.render();
        }
    }
}
