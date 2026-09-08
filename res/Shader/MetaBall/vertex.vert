#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 vertexColor;


out vec4 pos_cs;

void main()
{
    vec4 position_cs = /*proj * view * model * */position;
    pos_cs = position_cs;


    gl_Position = position_cs;

}
