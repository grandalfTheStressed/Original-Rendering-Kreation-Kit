package com.orkDevEngine.core.engine.screen.cameras;

import com.orkDevEngine.core.engine.screen.input.KeyboardManager;
import com.orkDevEngine.core.engine.screen.input.MouseManager;
import com.orkDevEngine.launcher.Main;
import imgui.ImGui;
import imgui.flag.ImGuiMouseCursor;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Camera {
    protected Vector3f pos, rotation;
    protected KeyboardManager keyboardManager;
    protected MouseManager mouseManager;

    public Camera(KeyboardManager keyboardManager, MouseManager mouseManager) {
        this(new Vector3f(0,0,0), new Vector3f(0,0,0), keyboardManager, mouseManager);
    }

    public Camera(Vector3f pos, Vector3f rotation, KeyboardManager keyboardManager, MouseManager mouseManager) {
        this.pos = pos;
        this.rotation = rotation;
        this.keyboardManager = keyboardManager;
        this.mouseManager = mouseManager;
    }

    public abstract void update(float interval);

    public void movePosition(Vector3f vec) {
        if(vec.z != 0) {
            pos.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * vec.z;
            pos.z += (float) Math.cos(Math.toRadians(rotation.y)) * vec.z;
        }
        if(vec.x != 0) {
            pos.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * vec.x;
            pos.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * vec.x;
        }
        pos.y += vec.y;
    }
    public void movePosition(float x, float y, float z) {
        this.movePosition(new Vector3f(x, y, z));
    }

    public void setPos(Vector3f vec) {
        this.pos = new Vector3f(vec);
    }

    public void setPos(float x, float y, float z) {
        this.pos = new Vector3f(x,y,z);
    }

    public void moveRotation(Vector3f vec) {
        this.rotation = rotation.add(vec);
    }

    public void moveRotation(float x, float y, float z) {
        moveRotation(new Vector3f(x,y,z));
    }

    public void setRotation(Vector3f vec) {
        this.rotation = new Vector3f(vec);
    }

    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x,y,z);
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
