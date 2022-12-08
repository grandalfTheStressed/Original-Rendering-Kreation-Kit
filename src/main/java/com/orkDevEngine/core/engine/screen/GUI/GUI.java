package com.orkDevEngine.core.engine.screen.GUI;

import com.orkDevEngine.core.engine.game.objects.entity.BaseEntity;
import com.orkDevEngine.core.engine.game.objects.managers.EntityManager;
import com.orkDevEngine.core.engine.game.objects.managers.GameObjectEntityManager;
import com.orkDevEngine.launcher.Main;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImFloat;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.glGetString;

public class GUI {

    private int width;
    private int height;

    private static String GPU;

    private EntityManager entityManager;

    /*
    if(ImGui.button("I am a button")) {
            showText = true;
        }
    if(showText) {

            ImGui.sameLine();
            if(ImGui.button("Hide This")) {
                showText = false;
            }
        }
     */
    public void prepareGUI() {
        if(GPU == null)
            GUI.setGPU(glGetString(GL_RENDERER));

        getDimensions();

        systemSpecsGui();

        entityManagerGui();
    }

    private void systemSpecsGui() {
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

    private void entityManagerGui() {
        if(entityManager == null) return;

        ImGui.begin("Entity Manager");

        List<BaseEntity> entityList = entityManager.getEntities();

        for (int i = 0; i < entityList.size(); i++) {
            BaseEntity entity = entityList.get(i);

            ImGui.text("Entity " + i);

            ImFloat xPos = new ImFloat(entity.getPos().x);
            ImFloat yPos = new ImFloat(entity.getPos().y);
            ImFloat zPos = new ImFloat(entity.getPos().z);
            ImFloat xRot = new ImFloat(entity.getRotation().x);
            ImFloat yRot = new ImFloat(entity.getRotation().y);
            ImFloat zRot = new ImFloat(entity.getRotation().z);

            ImGui.text("pos");
            setNextItemWidth1();
            pushStyleRed();
            ImGui.sameLine();
            if (ImGui.inputFloat("##x" + i, xPos, 1)) {
                entity.getPos().x = xPos.get();
            }
            ImGui.popStyleColor();

            setNextItemWidth1();
            pushStyleGreen();
            ImGui.sameLine();
            if (ImGui.inputFloat("##y" + i, yPos, 1)) {
                entity.getPos().y = yPos.get();
            }
            ImGui.popStyleColor();

            setNextItemWidth1();
            pushStyleBlue();
            ImGui.sameLine();
            if (ImGui.inputFloat("##z" + i, zPos, 1)) {
                entity.getPos().z = zPos.get();
            }
            ImGui.popStyleColor();

            ImGui.text("Rot");
            setNextItemWidth1();
            pushStyleRed();
            ImGui.sameLine();
            if (ImGui.inputFloat("##xRot" + i, xRot, 1)) {
                entity.setRotation(xRot.get(), entity.getRotation().y, entity.getRotation().z);
            }
            ImGui.popStyleColor();

            setNextItemWidth1();
            pushStyleGreen();
            ImGui.sameLine();
            if (ImGui.inputFloat("##yRot" + i, yRot, 1)) {
                entity.setRotation(entity.getRotation().x, yRot.get(), entity.getRotation().z);
            }
            ImGui.popStyleColor();

            setNextItemWidth1();
            pushStyleBlue();
            ImGui.sameLine();
            if (ImGui.inputFloat("##zRot" + i, zRot, 1)) {
                entity.setRotation(entity.getRotation().x, entity.getRotation().y, zRot.get());
            }
            ImGui.popStyleColor();

            if(ImGui.checkbox("Cullable##" + i, entity.isCullable())){
                entity.setCullable(!entity.isCullable());
            }

            ImGui.spacing();
        }

        ImGui.end();
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
    private void setNextItemWidth1() {
        ImGui.setNextItemWidth(width/12f);
    }

    public void getDimensions() {
        width = Main.getWindow().getWidth();
        height = Main.getWindow().getHeight();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static void setGPU(String GPU) {
        GUI.GPU = GPU;
    }
}
