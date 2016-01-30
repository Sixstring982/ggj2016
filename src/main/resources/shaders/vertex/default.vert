#version 330
precision highp float;
precision mediump int;

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;

out vec3 frag_pos;
out vec3 frag_normal;
out vec3 frag_eye;

uniform mat4 mvp;
uniform vec3 eye;

void main(void) {
    frag_normal = normal;
    frag_eye = eye;
    gl_Position = mvp * vec4(position, 1.0);
    frag_pos = position;
}