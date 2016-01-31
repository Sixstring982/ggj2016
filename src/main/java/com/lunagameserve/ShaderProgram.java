package com.lunagameserve;

import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by sixstring982 on 1/29/16.
 */
public class ShaderProgram {
    private int programId;
    private int vertId;
    private int fragId;
    private boolean ready = false;

    public void init(String vertPath, String fragPath) throws IOException {
        String vertSource = getResourceAsString(vertPath);
        String fragSource = getResourceAsString(fragPath);
        int status;

        vertId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertId, vertSource);
        glCompileShader(vertId);
        status = glGetShaderi(vertId, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(vertId));
            throw new RuntimeException("Cannot compile vertex shader.");
        }

        fragId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragId, fragSource);
        glCompileShader(fragId);
        status = glGetShaderi(fragId, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(fragId));
            throw new RuntimeException("Cannot compile fragment shader.");
        }

        programId = glCreateProgram();
        glAttachShader(programId, vertId);
        glAttachShader(programId, fragId);
        glLinkProgram(programId);
        status = glGetProgrami(programId, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            System.err.println(glGetProgramInfoLog(programId));
            throw new RuntimeException("Cannot link shader program.");
        }

        ready = true;
    }

    public void use() {
        glUseProgram(programId);
    }

    public void destroy() {
        if (ready) {
            glDeleteShader(vertId);
            glDeleteShader(fragId);
            glDeleteProgram(programId);
            ready = false;
        }
    }

    public void setMatrix4(Matrix4f matrix, String name) {
        int loc = glGetUniformLocation(programId, name);
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        matrix.get(buf);
        glUniformMatrix4fv(loc, false, buf);
    }

    private String getResourceAsString(String path) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(getClass().getResourceAsStream(path), writer, "UTF-8");
        return writer.toString();
    }


    public boolean isReady() {
        return ready;
    }

    public void setVector3(Vector3f vec, String name) {
        int loc = glGetUniformLocation(programId, name);
        FloatBuffer buf = BufferUtils.createFloatBuffer(3);
        vec.get(buf);
        glUniform3fv(loc, buf);
    }

    public void setFloat(float val, String name) {
        int loc = glGetUniformLocation(programId, name);
        glUniform1f(loc, val);
    }

    public void setVector2(Vector2f vec, String name) {
        int loc = glGetUniformLocation(programId, name);
        FloatBuffer buf = BufferUtils.createFloatBuffer(2);
        vec.get(buf);
        glUniform2fv(loc, buf);
    }

    public void setTextureUnit(Texture2D tex, String name) {
        int loc = glGetUniformLocation(programId, name);
        glUniform1i(loc, tex.getUnit());
    }
}
