#version 400 core

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGTHTS = 5;

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragPos;
in vec3 cameraDir;

out vec4 fragColor;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float hasTexture;
    float reflectance;
};

struct DirectionalLight {
    vec3 color;
    vec3 direction;
    float intensity;
};

struct PointLight {
    vec3 color;
    vec3 position;
    float intensity;
    float constant;
    float linear;
    float exponent;
};

struct SpotLight {
    vec3 color;
    vec3 position;
    vec3 conedir;
    float intensity;
    float constant;
    float linear;
    float exponent;
    float cutoff;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGTHTS];

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textCoords) {
    if(material.hasTexture == 1.0){
        diffuseC = texture(textureSampler, textCoords);
        specularC = normalize(diffuseC + material.specular);
    } else {
        diffuseC = material.diffuse;
        specularC = material.specular;
    }

    ambientC = vec4(diffuseC.xyz * ambientLight, diffuseC.w);

}

vec4 calcLightColor(vec3 lightColor, float lightIntensity, vec3 fragPos, vec3 toLightDir, vec3 fragNormal) {
    vec4 diffuseColor = vec4(0,0,0,0);
    vec4 specularColor = vec4(0,0,0,0);

    float diffuseFactor = max(dot(fragNormal, toLightDir), 0.0);
    diffuseColor = diffuseC * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

    vec3 fromLightDir = -toLightDir;
    vec3 reflectedLight = normalize(reflect(fromLightDir, fragNormal));
    float specularFactor = max(dot(-cameraDir, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularC * lightIntensity * specularFactor * material.reflectance * (lightColor, 1.0);

    return (diffuseColor + specularColor + ambientC);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 lightDir = light.position - position;
    vec3 toLightDir = normalize(lightDir);
    vec4 lightColor = calcLightColor(light.color, light.intensity, position, toLightDir, normal);

    float distance = length(lightDir);
    float attenuationInv = light.constant + light.linear * distance + light.exponent * distance * distance;
    return lightColor / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {
    vec3 lightDir = light.position - position;
    vec3 toLightDir = normalize(lightDir);
    vec3 fromLightDir = -toLightDir;
    float spotAlfa = dot(fromLightDir, normalize(light.conedir));

    vec4 color = vec4(0,0,0,0);

    if(spotAlfa > light.cutoff){
        color = calcPointLight(PointLight(light.color, light.position, light.intensity, light.constant, light.linear, light.exponent), position, normal);
        color *= (1.0 - (1.0 - spotAlfa) / (1.0 - light.cutoff));
    }

    return color;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 fragPos, vec3 fragNormal) {
    return calcLightColor(light.color, light.intensity, fragPos, normalize(light.direction), fragNormal);
}

void main() {

    setupColors(material, fragTextureCoord);

    vec4 phongColor = calcDirectionalLight(directionalLight, fragPos, fragNormal);

    for(int i = 0; i < MAX_POINT_LIGHTS; i++) {
        if(pointLights[i].intensity > 0) {
            phongColor += calcPointLight(pointLights[i], fragPos, fragNormal);
        }
    }
    for(int i = 0; i < MAX_SPOT_LIGTHTS; i++) {
        if(spotLights[i].intensity > 0) {
            phongColor += calcSpotLight(spotLights[i], fragPos, fragNormal);
        }
    }

    fragColor = phongColor;
}