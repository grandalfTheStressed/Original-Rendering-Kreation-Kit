package com.orkDevEngine.core.engine.screen;

import com.orkDevEngine.core.engine.EngineManager;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static com.orkDevEngine.core.engine.utils.Constants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class WindowManager {

    private static String title;

    private int width, height;
    private long windowPointer;

    private boolean resize, vsync;

    private final Matrix4f perspectiveProjection;
    private final Matrix4f orthoProjection;

    public WindowManager(String title, int width, int height, boolean vsync) {
        WindowManager.title = title;
        this.width = width;
        this.height = height;
        this.vsync = vsync;
        perspectiveProjection = new Matrix4f();
        orthoProjection = new Matrix4f();
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        boolean maximised = false;
        if(width == 0 || height == 0){
            width = 100;
            height = 100;
            glfwWindowHint(GLFW_MAXIMIZED, GL_TRUE);
            maximised = true;
        }

        updatePerspectiveProjection();

        windowPointer = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(windowPointer == MemoryUtil.NULL)
            throw new IllegalStateException("Can't create GLFW window");

        if(maximised){
            glfwMaximizeWindow(windowPointer);
        } else {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(windowPointer, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        glfwMakeContextCurrent(windowPointer);

        if(vsync)
            glfwSwapInterval(1);

        glfwShowWindow(windowPointer);

        GL.createCapabilities();
        glClearColor(0.3f,0.3f,0.3f,0);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        glfwSetFramebufferSizeCallback(windowPointer, (window, width, height) ->{
            this.width = width;
            this.height = height;
            glViewport(0,0,width, height);
            this.setResize(true);
            updatePerspectiveProjection();
        });
    }

    public void update() {
        setTitle(title + " FPS: " + EngineManager.getFps());
    }

    public void cleanUp() {
        glfwDestroyWindow(windowPointer);
    }

    public void setClearColor(float r, float g, float b, float a){
        glClearColor(r,g,b,a);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowPointer);
    }

    private void setTitle(String title) {
        glfwSetWindowTitle(windowPointer, title);
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindowPointer() {
        return windowPointer;
    }

    public Matrix4f getPerspectiveProjection() {
        return perspectiveProjection;
    }

    private void updatePerspectiveProjection() {
        float aspectRatio = ((float) width) / ((float) height);
        perspectiveProjection.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

    }

    public Matrix4f getOrthoProjection() {
        return orthoProjection;
    }

    private void updateOrthoProjection() {
        float aspectRatio = ((float) width) / ((float) height);
        orthoProjection.setOrtho2D(-1, 1, -1, 1);
    }
}
