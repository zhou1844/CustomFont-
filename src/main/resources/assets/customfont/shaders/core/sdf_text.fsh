#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;
uniform vec4 ColorModulator;
uniform float SdfWeight;

in vec2 texCoord0;
in vec4 vertexColor;
flat in ivec2 lightCoord;

out vec4 fragColor;

void main() {
    float dist = texture(Sampler0, texCoord0).r;
    float w = max(fwidth(dist), 0.75 / 255.0);
    float center = 0.5 - clamp(w * 0.35, 0.0, 0.18) - SdfWeight;
    float harden = smoothstep(0.015, 0.08, SdfWeight);
    float edgeWidth = mix(w * 0.75, w * 0.42, harden);
    float threshold = center + edgeWidth * 0.18;
    float alpha = step(threshold, dist);
    vec4 light = texelFetch(Sampler2, lightCoord / 16, 0);
    vec4 color = vertexColor * ColorModulator * light;
    if (alpha < 0.5) {
        discard;
    }
    fragColor = vec4(color.rgb, color.a);
}
