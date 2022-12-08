#version 400 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 fragTextureCoord;
out vec3 fragNormal;
out vec3 fragPos;
out vec3 cameraDir;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 rotationMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;

void main() {
    vec4 world_pos = transformationMatrix * vec4(position, 1.0);
    vec4 normaltransform = rotationMatrix * vec4(normal, 1.0);
    gl_Position = projectionMatrix * viewMatrix * world_pos;

    cameraDir = normalize(position - cameraPosition);
    fragNormal = normalize(normaltransform).xyz;
    fragPos = world_pos.xyz;
    fragTextureCoord = textureCoord;
}