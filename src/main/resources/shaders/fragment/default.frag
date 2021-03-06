#version 330

in vec3 frag_pos;
in vec3 frag_normal;
in vec3 frag_eye;
in vec2 frag_uv;
flat in float frag_sampleId;

vec3 eye = frag_eye;

out vec4 fragColor;

uniform float iGlobalTime;

uniform sampler2D sampler;
uniform sampler2D enemySampler;

vec4 lighting(in vec3 ro) {
    vec3 light = eye + vec3(0.0, 1.0, 0.0);
    vec3 N = frag_normal;
    vec3 V = normalize(eye - ro);
    vec3 L = normalize(light - ro);
    vec3 R = reflect(L, N);
    vec4 color;

    float diff = max(0.0, dot(L, N));
    float spec = max(0.0, pow(dot(R, V), 32.0));

    if (frag_sampleId < 0.5) {
        color = texture2D(sampler, frag_uv);
    } else if (frag_sampleId < 1.5) {
        color = texture2D(enemySampler, frag_uv);
    } else {
        color = vec4(1.0);
    }
    return vec4(color.rgb * (diff + spec), color.a);
}

void main(void) {
    fragColor = lighting(frag_pos);
}