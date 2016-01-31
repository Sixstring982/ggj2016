#version 300 es

layout(location = 0) in vec3 vert;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 uv;
layout(location = 3) in float sampleId;

out vec3 frag_normal;
out vec2 frag_uv;
flat out float frag_sampleId;

uniform mat4 mvp;

void main(void) {
  frag_uv = uv;
  frag_normal = normal;
  frag_sampleId = sampleId;
  gl_Position = mvp * vec4(vert, 1.0);
}
