#version 430 core

uniform vec4 lightPos;
vec4 lightColor = vec4(0.5);
uniform vec4 cameraPos;

in vec4 fragPos;
in vec4 color;
in vec4 uvCoord;
in vec4 normal;

out vec4 fragColor;

void main() {
	float ambientStrength = 0.1;
	vec4 ambient = ambientStrength * lightColor;

	// diffuse
	vec4 norm = normalize(normal);
	vec4 lightDir = normalize(lightPos - fragPos);
	float diff = max(dot(norm, lightDir), 0.0);
	vec4 diffuse = diff * lightColor;

	// specular
	float specularStrength = 0.5;
	vec4 viewDir = normalize(cameraPos - fragPos);
	vec4 reflectDir = reflect(-lightDir, norm);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
	vec4 specular = specularStrength * spec * lightColor;

	vec4 result = (ambient + diffuse + specular) * color;
	fragColor = result;
}

