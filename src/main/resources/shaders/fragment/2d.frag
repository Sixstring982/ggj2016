#version 330

out vec4 fragColor;

in vec3 frag_normal;
in vec2 frag_uv;
flat in float frag_sampleId;

uniform sampler2D enemySampler;
uniform sampler2D boxSampler;

void main(void) {
    if (frag_sampleId < 0.1) {
        fragColor = texture2D(enemySampler, frag_uv);
    } else if (frag_sampleId < 2.0) {
        fragColor = texture2D(boxSampler, frag_uv);
    } else {
        fragColor = vec4(1.0, 0.0, 0.0, 1.0);
    }
}