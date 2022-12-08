package com.orkDevEngine.core.engine.screen.cameras;

import com.orkDevEngine.core.engine.screen.input.KeyboardManager;
import com.orkDevEngine.core.engine.screen.input.MouseManager;
import com.orkDevEngine.launcher.Main;
import imgui.ImGui;
import imgui.flag.ImGuiMouseCursor;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.orkDevEngine.core.engine.utils.Constants.MOUSE_SENSITIVIY;
import static org.lwjgl.glfw.GLFW.*;

public class EditCamera extends Camera{

    private boolean flyMode = false;
    public EditCamera(KeyboardManager keyboardManager, MouseManager mouseManager)  {
        super(keyboardManager, mouseManager);
    }

    public EditCamera(Vector3f pos, Vector3f rotation, KeyboardManager keyboardManager, MouseManager mouseManager) {
        super(pos, rotation, keyboardManager, mouseManager);
    }

    @Override
    public void update(float interval) {

        Vector2f displVec = mouseManager.getDisplVec();
        float cameraStep = 20f * interval;

        if(mouseManager.isButtonReleased(GLFW_MOUSE_BUTTON_RIGHT)) {

            flyMode = !flyMode;

            if(flyMode)
                mouseManager.disableCursor();
            else
                mouseManager.enableCursor();
        }

        Vector3f cameraInc = new Vector3f(0,0,0);

        if(keyboardManager.isKeyDown(GLFW_KEY_LEFT_SHIFT))
            cameraInc.y = 1;
        if(keyboardManager.isKeyDown(GLFW_KEY_LEFT_CONTROL))
            cameraInc.y = -1;

        movePosition(cameraInc.mul(cameraStep));

        if(flyMode) {
            cameraInc = new Vector3f(0,0,0);
            //wasd movement shift/ctrl for altitude
            if(keyboardManager.isKeyDown(GLFW_KEY_W))
                cameraInc.z = -1;
            if(keyboardManager.isKeyDown(GLFW_KEY_A))
                cameraInc.x = -1;
            if(keyboardManager.isKeyDown(GLFW_KEY_S))
                cameraInc.z = 1;
            if(keyboardManager.isKeyDown(GLFW_KEY_D))
                cameraInc.x = 1;

            //When mouse moves we want to rotate around the opposing axis, y movement should rotate around the x axis, x movement should rotate around the y axis

            moveRotation(new Vector3f(displVec.y, displVec.x, 0).mul(MOUSE_SENSITIVIY));
            movePosition(cameraInc.mul(cameraStep));
        } else {
            cameraInc = new Vector3f(0,0,0);
            if(mouseManager.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {

                mouseManager.setCursor(ImGuiMouseCursor.Hand);

                if(displVec.x != 0 || displVec.y != 0) {
                    cameraInc.x = -displVec.x;
                    cameraInc.z = -displVec.y;

                    movePosition(cameraInc.normalize().mul(cameraStep * 2));
                }
            }
            else if(mouseManager.isButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
                cameraInc = new Vector3f(0,0,0);
                mouseManager.setCursor(ImGuiMouseCursor.ResizeNWSE);
                moveRotation(new Vector3f(-displVec.y, -displVec.x, 0).mul(MOUSE_SENSITIVIY));
                movePosition(cameraInc.mul(cameraStep));
            } else if(mouseManager.getScroll() != 0) {
                cameraInc = new Vector3f(0,0,0);
                cameraInc.z = mouseManager.getScroll() > 0 ? -3 : 3;
                movePosition(cameraInc.mul(cameraStep));
            }
            else {
                mouseManager.setCursor(ImGuiMouseCursor.Arrow);
            }
        }
    }
}
