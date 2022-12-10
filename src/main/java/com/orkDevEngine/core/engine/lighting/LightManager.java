package com.orkDevEngine.core.engine.lighting;

import com.orkDevEngine.core.engine.game.objects.managers.EntityManager;
import com.orkDevEngine.core.engine.lighting.lights.DirectionalLight;
import com.orkDevEngine.core.engine.lighting.lights.PointLight;
import com.orkDevEngine.core.engine.lighting.lights.SpotLight;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class LightManager extends EntityManager {

    private DirectionalLight directionalLight;
    private List<PointLight> pointLights;
    private List<SpotLight> spotLights;

    public LightManager() {
    }

    public void init() {
        float lightIntensity = 1.0f;
        //PointLight
        Vector3f lightPosition = new Vector3f(-.5f, -.5f, -3.2f);
        Vector3f lightColor = new Vector3f(1f,1f,1f);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1);

        //SpotLight
        Vector3f coneDir = new Vector3f(.4f,0,-1);
        float cutoff = (float) Math.cos(Math.toRadians(180));
        SpotLight spotLight0 = new SpotLight(new PointLight(lightColor, new Vector3f(-10f,-.5f,-6f), lightIntensity, 0,1,0), coneDir, cutoff);

        //Directional Light
        lightColor = new Vector3f(1f, 1f, 1f);
        directionalLight = new DirectionalLight(lightColor, 1f);

        pointLights = new ArrayList<>();
        spotLights = new ArrayList<>();

        pointLights.add(pointLight);
        spotLights.add(spotLight0);
    }

    public void update() {
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }

    public List<SpotLight> getSpotLights() {
        return spotLights;
    }
}
