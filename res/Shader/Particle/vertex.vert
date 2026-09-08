#version 430 core

layout(std430, binding = 0) buffer Pos {
    vec4 positions[];
};

layout(std430, binding = 2) buffer Col {
    vec4 colors[];
};

out vec4 color;

uniform vec2 windowSize;

void main() {
    gl_Position = vec4(positions[gl_VertexID].xy / (windowSize / 2), 0.0, 1.0);
    gl_PointSize = 5.0;
    color = colors[gl_VertexID];
}
