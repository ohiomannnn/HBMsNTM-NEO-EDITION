#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec2 UV0;
in vec3 Normal;

uniform vec4 Color;
uniform ivec2 UV1;
uniform ivec2 UV2;

uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 PoseMat;
uniform mat4 TextureMat;
uniform int FogShape;

uniform int EnableLight;

uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec4 lightMapColor;
out vec4 overlayColor;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vec4 pos = PoseMat * vec4(Position, 1.0);
    vec3 normalPos = normalize(mat3(PoseMat) * Normal);

    vertexDistance = fog_distance(pos.xyz, FogShape);
    if (EnableLight == 1) {
        vertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, normalPos, Color);
    } else {
        vertexColor = Color;
    }
    lightMapColor = texelFetch(Sampler2, UV2 / 16, 0);
    overlayColor = texelFetch(Sampler1, UV1, 0);
    texCoord0 = (TextureMat * vec4(UV0, 0.0, 1.0)).xy;
}