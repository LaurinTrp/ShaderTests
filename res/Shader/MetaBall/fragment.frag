#version 430 core

out vec4 fragColor;

in vec4 pos_cs;

uniform float mouse_x, mouse_y;

const int ballcount = 20;

uniform vec2 balls[ballcount];
uniform vec2 ball0, ball1;

void main() {
	vec2 mouse = vec2(mouse_x, mouse_y);

	float radius = 0.05;

	float sum = 0.0;
	for(int i = 0; i < ballcount; i++){
		float diff = distance(pos_cs.xy, balls[i]);
		sum += 0.5 * radius / diff;
	}

	vec4 color = vec4(sum, 0.0, 1.0 - sum, 1.0);

	fragColor = color;
}
