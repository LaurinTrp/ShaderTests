#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 uvCoordinate;
layout(location = 2) in vec4 vertexColor;


out vec4 color;

void main() {
    color = vertexColor;

    gl_Position = position;

}
