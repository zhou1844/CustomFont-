#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 texCoord0;
out vec4 vertexColor;
flat out ivec2 lightCoord;

void main() {
    vec4 viewPos = ModelViewMat * vec4(Position, 1.0);
    viewPos.z += 0.005;
    gl_Position = ProjMat * viewPos;
    texCoord0 = UV0;
    vertexColor = Color;
    lightCoord = UV2;
}
