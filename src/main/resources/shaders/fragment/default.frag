#version 330

in vec3 frag_pos;
in vec3 frag_normal;
in vec3 frag_eye;
in vec2 frag_uv;

vec3 eye = frag_eye;

out vec4 fragColor;

uniform float iGlobalTime;
uniform sampler2D sampler;

vec3 lighting(in vec3 ro) {
    vec3 light = eye + vec3(0.0, 1.0, 0.0);
    vec3 N = frag_normal;
    vec3 V = normalize(eye - ro);
    vec3 L = normalize(light - ro);
    vec3 R = reflect(L, N);

    float diff = max(0.0, dot(L, N));
    float spec = max(0.0, pow(dot(R, V), 32.0));

    return texture2D(sampler, frag_uv).rgb * (diff + spec);
}

void main(void) {
    fragColor = vec4(lighting(frag_pos), 1.0);
}