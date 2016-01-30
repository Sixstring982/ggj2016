#version 330

in vec3 frag_pos;
in vec3 frag_normal;

out vec4 fragColor;

uniform float iGlobalTime;
uniform vec3 eye;

vec3 lighting(in vec3 ro) {
    vec3 light = vec3(sin(iGlobalTime), 1.0, cos(iGlobalTime));
    vec3 N = frag_normal;
    vec3 V = normalize(eye - ro);
    vec3 L = normalize(light - ro);
    vec3 R = reflect(-L, N);

    float diff = max(0.0, dot(L, N));
    float spec = max(0.0, pow(dot(R, V), 32.0));

    return vec3(1.0) * (diff + spec);
}

void main(void) {
    fragColor = vec4(lighting(frag_pos), 1.0);
}