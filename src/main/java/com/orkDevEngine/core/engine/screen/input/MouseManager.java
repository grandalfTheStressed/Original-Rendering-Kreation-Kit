package com.orkDevEngine.core.engine.screen.input;

import com.orkDevEngine.launcher.Main;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiMouseCursor;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseManager {

    private final Vector2d previousPos, currentPos;
    private final Vector2f displVec;

    private int currentCursor;
    private final int BUTTONS_COUNT = 3;

    private boolean[] buttons = new boolean[BUTTONS_COUNT];
    private boolean[] buttonsLast = new boolean[BUTTONS_COUNT];

    private double scroll;
    private boolean inWindow = false;

    public MouseManager() {
        previousPos = new Vector2d(-1,-1);
        currentPos = new Vector2d(0,0);
        displVec = new Vector2f();
    }

    public void init() {

        glfwSetCursorPosCallback(Main.getWindow().getWindowPointer(), (window, xpos, ypos) -> {
            currentPos.x = (xpos + previousPos.x) * .5;
            currentPos.y = (ypos + previousPos.y) * .5;
        });

        glfwSetCursorEnterCallback(Main.getWindow().getWindowPointer(), (window, entered) -> {
            inWindow = entered;
        });

        glfwSetScrollCallback(Main.getWindow().getWindowPointer(), (window, dx, dy) -> {
            // Store dx and dy here (both are double values)
            if(!ImGui.getIO().getWantCaptureMouse()) {
                scroll = dy;
            }
        });
        glfwSetMouseButtonCallback(Main.getWindow().getWindowPointer(), (window, button, action, mods) -> {
            if(!ImGui.getIO().getWantCaptureMouse()) {
                if (action == GLFW_RELEASE) {
                    buttons[button] = false;
                    buttonsLast[button] = true;
                }
                if (action == GLFW_PRESS)
                    buttons[button] = true;
            }
        });
    }

    public void input() {

        buttonsLast = new boolean[BUTTONS_COUNT];

        displVec.x = 0;
        displVec.y = 0;

        if(!previousPos.equals(currentPos)) {

            double x = currentPos.x - previousPos.x;
            double y = currentPos.y - previousPos.y;
            boolean rotateX = x != 0;
            boolean rotateY = y != 0;

            if(rotateX)
                displVec.y = (float) y;
            if(rotateY)
                displVec.x = (float) x;
        }

        scroll = 0;
        previousPos.x = currentPos.x;
        previousPos.y  = currentPos.y;
    }

    public boolean isButtonReleased(int keyCode){
        return buttonsLast[keyCode] && !buttons[keyCode];
    }

    public boolean isButtonDown(int keyCode){
        return buttons[keyCode];
    }

    public boolean isButtonUp(int keyCode){
        return !isButtonDown(keyCode);
    }

    public boolean isInWindow() {
        return inWindow;
    }

    public double getScroll() {
        return scroll;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void setCursor(int cursorType) {
        if(cursorType == currentCursor) return;

        ImGui.setMouseCursor(cursorType);
        currentCursor = cursorType;
    }

    public void setCursorPos(double x, double y){
        glfwSetCursorPos(Main.getWindow().getWindowPointer(), x, y);
    }

    public void centerCursor() {
        setCursorPos(Main.getWindow().getWidth() / 2.0, (Main.getWindow().getHeight() / 2.0));
    }

    public void disableCursor() {
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NoMouse);
        glfwSetInputMode(Main.getWindow().getWindowPointer(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        centerCursor();
    }

    public void enableCursor() {
        ImGui.getIO().removeConfigFlags(ImGuiConfigFlags.NoMouse);
        glfwSetInputMode(Main.getWindow().getWindowPointer(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        centerCursor();
    }
}
