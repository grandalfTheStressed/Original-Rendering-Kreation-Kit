package com.orkDevEngine.core.engine.screen.input;

import com.orkDevEngine.launcher.Main;
import imgui.ImGui;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardManager {

    private static boolean inputEnabled = true;
    private static final int NUM_KEYS = 65535;
    private static boolean[] keysLast = new boolean[NUM_KEYS];
    private static boolean[] keys = new boolean[NUM_KEYS];
    public KeyboardManager() {
    }

    public void init() {
        setCallbacks();
    }

    public void setCallbacks() {


        GLFW.glfwSetKeyCallback(Main.getWindow().getWindowPointer(), ((window, key, scancode, action, mods) -> {
            if(!ImGui.getIO().getWantCaptureKeyboard()) {
                if (KeyboardManager.inputEnabled) {
                    if (action == GLFW_RELEASE) {
                        KeyboardManager.keys[key] = false;
                        KeyboardManager.keysLast[key] = true;
                    }
                    if (action == GLFW_PRESS)
                        KeyboardManager.keys[key] = true;
                }
            }
        }));
    }

    public void input() {
        keysLast = new boolean[NUM_KEYS];
    }

    public boolean isKeyReleased(int keyCode){
        return keysLast[keyCode] && !keys[keyCode];
    }

    public boolean isKeyDown(int keyCode){
        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode){
        return !isKeyDown(keyCode);
    }

    public void disableInput() {
        KeyboardManager.inputEnabled = false;
    }

    public void enableInput() {
        KeyboardManager.inputEnabled = true;
    }
}
