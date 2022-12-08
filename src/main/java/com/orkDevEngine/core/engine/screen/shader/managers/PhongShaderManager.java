package com.orkDevEngine.core.engine.screen.shader.managers;

import com.orkDevEngine.core.engine.lighting.LightManager;
import com.orkDevEngine.core.engine.utils.Utilities;
import com.orkDevEngine.core.engine.utils.exceptions.ShaderManagerException;
import com.orkDevEngine.launcher.Main;

import static com.orkDevEngine.core.engine.utils.Constants.AMBIENT_LIGHT;
import static com.orkDevEngine.core.engine.utils.Constants.SPECULAR_POWER;

public class PhongShaderManager extends ShaderManager{

    LightManager lightManager;
    public PhongShaderManager(LightManager lightManager) {
        super();

        this.lightManager = lightManager;
    }

    @Override
    public void init() throws ShaderManagerException{
        super.init();
        try {
            this.createVertexShader(Utilities.loadResource("/shaders/entityVertex.vs"));
            this.createFragmentShader(Utilities.loadResource("/shaders/phongFragment.fs"));
            this.link();
            uniformManager.createUniform("textureSampler");
            uniformManager.createUniform("cameraPosition");
            uniformManager.createUniform("rotationMatrix");
            uniformManager.createUniform("transformationMatrix");
            uniformManager.createUniform("projectionMatrix");
            uniformManager.createUniform("viewMatrix");
            uniformManager.createUniform("ambientLight");
            uniformManager.createMaterialUniform("material");
            uniformManager.createUniform("specularPower");
            uniformManager.createDirectionalLightUniform("directionalLight");
            uniformManager.createPointLightListUniform("pointLights", 5);
            uniformManager.createSpotLightListUniform("spotLights", 5);
        } catch (Exception e) {
            throw new ShaderManagerException(e.getMessage());
        }
    }

    public void bindLighting() {
        uniformManager.setUniform("ambientLight", AMBIENT_LIGHT);
        uniformManager.setUniform("specularPower", SPECULAR_POWER);
        uniformManager.setUniform("directionalLight", lightManager.getDirectionalLight());
        uniformManager.setUniformPointLightList("pointLights", lightManager.getPointLights());
        uniformManager.setUniformSpotlightList("spotLights", lightManager.getSpotLights());
    }
}
