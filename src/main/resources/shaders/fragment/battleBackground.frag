#version 300 es
precision highp float;
precision mediump int;

out vec4 fragColor;

vec2 fragCoord = gl_FragCoord.xy;

uniform vec2 iResolution;
uniform float iGlobalTime;

void main(void) {
  vec2 uv = ((fragCoord.xy / iResolution.xy) - vec2(0.5)) * 2.0;

  float intentisy = 0.5 + 0.5 * sin(length(uv) * 20.0 + iGlobalTime);
  float theta = dot(vec2(1.0, 0.0), uv * vec2(sin(iGlobalTime), cos(iGlobalTime)));
  vec3 color = vec3(0.2, 0.0, 0.4) * clamp(pow(sin(theta) + tan(intentisy), 4.0), 0.0, 1.0);
  fragColor = vec4(color,1.0);
}
