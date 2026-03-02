#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;
uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 vertexColor;
flat in ivec2 lightCoord;

out vec4 fragColor;

void main() {
    float dist = texture(Sampler0, texCoord0).r;
    float w = max(fwidth(dist), 1.0 / 255.0);
    float alpha = smoothstep(0.5 - w, 0.5 + w, dist);
    vec4 light = texelFetch(Sampler2, lightCoord / 16, 0);
    vec4 color = vertexColor * ColorModulator * light;
    fragColor = vec4(color.rgb, color.a * alpha);
}
