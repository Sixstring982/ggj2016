#version 300 es
precision highp float;
precision mediump int;

out vec4 fragColor;

vec2 fragCoord = gl_FragCoord.xy;

uniform vec2 iResolution;
uniform float iGlobalTime;

void main(void) {
  vec2 uv = fragCoord / iResolution.xy;
  fragColor = vec4(uv, 0.5 + 0.5 * sin(iGlobalTime), 1.0);
}
