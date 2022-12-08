package com.orkDevEngine.core.engine.screen.shader.managers;

import com.orkDevEngine.core.engine.game.objects.Material;
import com.orkDevEngine.core.engine.lighting.lights.DirectionalLight;
import com.orkDevEngine.core.engine.lighting.lights.PointLight;
import com.orkDevEngine.core.engine.lighting.lights.SpotLight;
import com.orkDevEngine.core.engine.utils.exceptions.UniformManagerException;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class UniformManager {

    int programPointer;
    private final Map<String, Integer> uniforms;

    public UniformManager(int programPointer) {
        this.programPointer = programPointer;
        uniforms = new HashMap<>();
    }
    public void createUniform(String uniformName) throws UniformManagerException {
        int uniformLocation = glGetUniformLocation(programPointer, uniformName);

        if(uniformLocation < 0)
            throw new UniformManagerException("Could not find uniform " + uniformName);

        uniforms.put(uniformName, uniformLocation);
    }

    public void createMaterialUniform(String uniformName) throws UniformManagerException {
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void createDirectionalLightUniform(String uniformName) throws UniformManagerException {
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".color");
    }

    public void createPointLightUniform(String uniformName) throws UniformManagerException {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".constant");
        createUniform(uniformName + ".linear");
        createUniform(uniformName + ".exponent");
    }

    public void createPointLightListUniform(String uniformName, int size) throws UniformManagerException {
        for(int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightUniform(String uniformName) throws UniformManagerException {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".constant");
        createUniform(uniformName + ".linear");
        createUniform(uniformName + ".exponent");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void createSpotLightListUniform(String uniformName, int size) throws UniformManagerException {
        for(int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, boolean value) {

        glUniform1f(uniforms.get(uniformName), value ? 1.0f : 0.0f);
    }

    public void setUniform(String uniformName, float value) {

        glUniform1f(uniforms.get(uniformName), value);
    }
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.hasTexture());
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void setUniform(String uniformName, DirectionalLight light) {
        setUniform(uniformName + ".intensity", light.getIntensity());
        setUniform(uniformName + ".direction", light.getPosition());
        setUniform(uniformName + ".color", light.getColor());
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".constant", pointLight.getConstant());
        setUniform(uniformName + ".linear", pointLight.getLinear());
        setUniform(uniformName + ".exponent", pointLight.getExponent());
    }

    public void setUniformPointLightList(String uniformName, List<PointLight> pointLights) {
        int lightCount = pointLights != null ? pointLights.size() : 0;
        for(int i = 0; i < lightCount; i++) {
            setUniform(uniformName + "[" + i + "]", pointLights.get(i));
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".color", spotLight.getColor());
        setUniform(uniformName + ".position", spotLight.getPosition());
        setUniform(uniformName + ".intensity", spotLight.getIntensity());
        setUniform(uniformName + ".constant", spotLight.getConstant());
        setUniform(uniformName + ".linear", spotLight.getLinear());
        setUniform(uniformName + ".exponent", spotLight.getExponent());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutoff());
    }

    public void setUniformSpotlightList(String uniformName, List<SpotLight> spotLights) {
        int lightCount = spotLights != null ? spotLights.size() : 0;
        for(int i = 0; i < lightCount; i++) {
            setUniform(uniformName + "[" + i + "]", spotLights.get(i));
        }
    }
}
