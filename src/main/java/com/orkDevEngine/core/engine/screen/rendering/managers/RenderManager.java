package com.orkDevEngine.core.engine.screen.rendering.managers;

import com.orkDevEngine.core.engine.game.objects.managers.ActorManager;
import com.orkDevEngine.core.engine.lighting.LightManager;
import com.orkDevEngine.core.engine.screen.GUI.GUI;
import com.orkDevEngine.core.engine.screen.cameras.Camera;
import com.orkDevEngine.core.engine.screen.shader.managers.PhongShaderManager;
import com.orkDevEngine.core.engine.screen.rendering.renderers.EntityRenderer;
import com.orkDevEngine.core.engine.utils.Transformation;
import com.orkDevEngine.core.engine.utils.exceptions.RendererException;
import com.orkDevEngine.core.engine.utils.exceptions.ShaderManagerException;
import com.orkDevEngine.launcher.Main;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;

public class RenderManager implements IRenderManager{

    private final ImGuiImplGlfw imGuiGlfw;
    private final ImGuiImplGl3 imGuiGl3;
    private final GUI gui;
    private final PhongShaderManager shaderManager;

    private final EntityRenderer entityRenderer;

    private ActorManager actorManager;

    public RenderManager(LightManager lightManager, ActorManager actorManager) {
        this.actorManager = actorManager;
        this.entityRenderer = new EntityRenderer(actorManager);
        this.imGuiGlfw = new ImGuiImplGlfw();
        this.imGuiGl3 = new ImGuiImplGl3();
        this.gui = new GUI();
        this.gui.setActorManager(actorManager);
        this.gui.setLightManager(lightManager);
        this.shaderManager = new PhongShaderManager(lightManager);
    }

    @Override
    public void init() throws RendererException {
        try {
            this.shaderManager.init();
        } catch (ShaderManagerException e) {
            throw new RendererException(e.getMessage());
        }
        this.entityRenderer.init();

        initImGui();
        imGuiGlfw.init(Main.getWindow().getWindowPointer(), false);
        imGuiGl3.init();
    }

    @Override
    public void render(Camera camera) {
        clear();

        shaderManager.bind();

        prepareView(camera);

        entityRenderer.render(shaderManager.getUniformManager());

        shaderManager.unbind();

        renderGUI();

    }

    protected void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    protected void renderGUI() {

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        gui.prepareGUI();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        glfwSwapBuffers(Main.getWindow().getWindowPointer());
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    }

    @Override
    public void prepareView(Camera camera) {
        shaderManager.getUniformManager().setUniform("projectionMatrix", Main.getWindow().getPerspectiveProjection());
        shaderManager.getUniformManager().setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        shaderManager.getUniformManager().setUniform("cameraPosition", camera.getPos());
        shaderManager.bindLighting();
    }

    @Override
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void cleanUp() {
        shaderManager.cleanup();
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
