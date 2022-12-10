package com.orkDevEngine.core.engine.screen.GUI;

import com.orkDevEngine.core.engine.game.objects.entity.Actor;
import com.orkDevEngine.core.engine.game.objects.entity.BaseEntity;
import com.orkDevEngine.core.engine.game.objects.managers.ActorManager;
import com.orkDevEngine.core.engine.lighting.LightManager;
import com.orkDevEngine.core.engine.lighting.lights.DirectionalLight;
import com.orkDevEngine.core.engine.lighting.lights.PointLight;
import com.orkDevEngine.core.engine.lighting.lights.SpotLight;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.type.ImFloat;
import org.apache.commons.collections4.CollectionUtils;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.glGetString;

public class GUI {

    private int width = 800;

    private static String GPU;

    private ActorManager actorManager;

    private LightManager lightManager;

    public void prepareGUI() {
        if(GPU == null)
            GUI.setGPU(glGetString(GL_RENDERER));

        renderSystemSpecsGui();

        renderEntityManagerGui();
    }

    private void renderSystemSpecsGui() {
        String freeMemory = "Free memory (GB): " + Runtime.getRuntime().freeMemory() / (1024.0f * 1024.0f * 1024.0f);
        String maxMemory = "Max memory (GB): " + Runtime.getRuntime().maxMemory() / (1024.0f * 1024.0f * 1024.0f);
        String availableMemory = "Available Memory (GB): " + Runtime.getRuntime().totalMemory() / (1024.0f * 1024.0f * 1024.0f);

        ImGui.begin("System Specs");

        ImGui.text("GPU: " + GPU);
        ImGui.text(freeMemory);
        ImGui.text(maxMemory);
        ImGui.text(availableMemory);

        ImGui.end();
    }

    private void renderEntityManagerGui() {

        ImGui.begin("Entity Manager");

        width = (int) ImGui.getContentRegionMaxX();

        if(ImGui.collapsingHeader("Actors")){

            if(actorManager != null) {
                actorGui();
            }
        }

        if(ImGui.collapsingHeader("Lights")) {
            if (lightManager != null) {
                lightGui();
            }
        }

        ImGui.end();
    }

    private void actorGui() {
        List<BaseEntity> entityList = actorManager.getEntities();

        for (int i = 0; i < entityList.size(); i++) {
            Actor actor = (Actor)entityList.get(i);
            ImFloat reflectanceFloat = new ImFloat(actor.getModel().getMaterial().getReflectance());
            Vector3f vec = actor.getPosition();

            ImGui.text("Actor " + i);

            if(makeXYZPanel("##EntityPos" + i, vec)) {
                actor.setPosition(vec);
            }

            ImGui.sameLine();
            ImGui.text("Position");

            vec = actor.getRotation();

            if(makeXYZPanel("##EntityRot" + i, vec)) {
                actor.setRotation(vec);
            }
            ImGui.sameLine();
            ImGui.text("Rotation");

            ImFloat scale = new ImFloat(actor.getScale());
            setNextItemWidth(2,3);
            if(ImGui.inputFloat("Scale##actor" + i, scale, .1f)){
                actor.setScale(scale.get());
            }

            setNextItemWidth(2,3);
            if (ImGui.sliderFloat("##reflectanceActor" + i, reflectanceFloat.getData(), 0, 1)) {
                if(reflectanceFloat.get() < .1f)
                    reflectanceFloat.set(.1f);

                actor.getModel().getMaterial().setReflectance(reflectanceFloat.get());
            }

            ImGui.sameLine();
            ImGui.text("Reflectance");

            if (ImGui.checkbox("Cullable##" + i, actor.isCullable())) {
                actor.setCullable(!actor.isCullable());
            }

            ImGui.spacing();
        }
    }

    private void lightGui() {
        DirectionalLight directionalLight = lightManager.getDirectionalLight();

        if(directionalLight != null) {
            ImFloat intensityFloat = new ImFloat(directionalLight.getIntensity());
            Vector3f vec = directionalLight.getPosition();

            ImGui.text("Dir Light");

            if(makeXYZPanel("##dirLightPos", vec)) {
                directionalLight.setPosition(vec);
            }

            ImGui.sameLine();
            ImGui.text("Direction");

            setNextItemWidth(2,3);
            if (ImGui.sliderFloat("##DirLightIntensity", intensityFloat.getData(), 0, 1)) {
                directionalLight.setIntensity(intensityFloat.get());
            }

            ImGui.sameLine();
            ImGui.text("Intensity");

            setNextItemWidth(2,3);
            float[] color = new float[]{directionalLight.getColor().x,directionalLight.getColor().y,directionalLight.getColor().z};
            if(ImGui.colorEdit3("Color##DirLight", color)) {
                directionalLight.setColor(new Vector3f(color[0], color[1], color[2]));
            }
        }

        List<PointLight> pointLights = lightManager.getPointLights();

        if(CollectionUtils.isNotEmpty(pointLights)) {
            for(int i = 0; i < pointLights.size(); i++) {
                PointLight pointLight = pointLights.get(0);

                ImFloat intensityFloat = new ImFloat(pointLight.getIntensity());
                ImFloat linearFloat = new ImFloat(pointLight.getLinear());
                ImFloat constantFloat = new ImFloat(pointLight.getConstant());
                ImFloat exponentFloat = new ImFloat(pointLight.getExponent());
                Vector3f vec = pointLight.getPosition();

                ImGui.text("Point Light " + i);

                if(makeXYZPanel("##pointLight" + i, vec)) {
                    pointLight.setPosition(vec);
                }

                ImGui.sameLine();
                ImGui.text("Position");

                setNextItemWidth(2,3);
                if (ImGui.sliderFloat("##PointLightIntensity" + i, intensityFloat.getData(), 0, 1)) {
                    pointLight.setIntensity(intensityFloat.get());
                }

                ImGui.sameLine();
                ImGui.text("Intensity");

                setNextItemWidth(2,3);
                if (ImGui.sliderFloat("##PointLightLinear" + i, linearFloat.getData(), 0, 1)) {
                    pointLight.setLinear(linearFloat.get());
                }

                ImGui.sameLine();
                ImGui.text("Linear");

                setNextItemWidth(2,3);
                if (ImGui.sliderFloat("##PointLightConstant" + i, constantFloat.getData(), 0, 1)) {
                    pointLight.setConstant(constantFloat.get());
                }

                ImGui.sameLine();
                ImGui.text("Constant");

                setNextItemWidth(2,3);
                if (ImGui.sliderFloat("##PointLightExponent" + i, exponentFloat.getData(), 0, 1)) {
                    pointLight.setExponent(exponentFloat.get());
                }

                ImGui.sameLine();
                ImGui.text("Exponent");

                setNextItemWidth(2,3);
                float[] color = new float[]{pointLight.getColor().x,pointLight.getColor().y,pointLight.getColor().z};
                if(ImGui.colorEdit3("Color##PointLight" + i, color)) {
                    pointLight.setColor(new Vector3f(color[0], color[1], color[2]));
                }

            }
        }

        List<SpotLight> spotLights = lightManager.getSpotLights();

        if(CollectionUtils.isNotEmpty(pointLights)) {

        }
    }

    private boolean makeXYZPanel(String label, Vector3f vec) {
        boolean flag = false;
        ImFloat x = new ImFloat(vec.x);
        ImFloat y = new ImFloat(vec.y);
        ImFloat z = new ImFloat(vec.z);
        setNextItemWidth(2,9);
        pushStyleRed();
        if (ImGui.inputFloat(label + "x", x, 1f)) {
            flag = true;
        }
        ImGui.popStyleColor();

        setNextItemWidth(2,9);
        pushStyleGreen();
        ImGui.sameLine(2*width/9f + 9);
        if (ImGui.inputFloat(label + "y", y, 1f)) {
            flag = true;
        }
        ImGui.popStyleColor();

        setNextItemWidth(2,9);
        pushStyleBlue();
        ImGui.sameLine(4*width/9f + 9);
        if (ImGui.inputFloat(label + "z", z, 1f)) {
            flag = true;
        }
        ImGui.popStyleColor();

        vec.x = x.get();
        vec.y = y.get();
        vec.z = z.get();

        return flag;
    }

    private void pushStyleRed() {
        ImGui.pushStyleColor(ImGuiCol.Text, 1f, 0f,0f, 1f);
    }

    private void pushStyleGreen() {
        ImGui.pushStyleColor(ImGuiCol.Text, 0f, 1f,0f, 1f);
    }

    private void pushStyleBlue() {
        ImGui.pushStyleColor(ImGuiCol.Text, .5f, .6f,1f, 1f);
    }


    private void setNextItemWidth(float f1, float f2) {

        ImGui.setNextItemWidth(width*(f1/f2));
    }

    public void setActorManager(ActorManager actorManager) {
        this.actorManager = actorManager;
    }

    public void setLightManager(LightManager lightManager) {
        this.lightManager = lightManager;
    }

    public static void setGPU(String GPU) {
        GUI.GPU = GPU;
    }
}
