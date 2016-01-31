#version 300 es

layout(location = 0) in vec4 vert;

void main(void) {
  gl_Position = vert;
}
