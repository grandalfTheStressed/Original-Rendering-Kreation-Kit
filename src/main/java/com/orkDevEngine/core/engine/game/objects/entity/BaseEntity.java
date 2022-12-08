package com.orkDevEngine.core.engine.game.objects.entity;

import com.orkDevEngine.core.engine.game.objects.Model;
import org.joml.Vector3f;

public abstract class BaseEntity {

    private Model model;
    private Vector3f pos, rotation;
    private float scale;

    private Boolean cullable;

    public BaseEntity(Model model, Vector3f pos, Vector3f rotation, float scale, boolean cullable) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
        this.cullable = cullable;
    }

    public void addPos(Vector3f vec) {
        pos = pos.add(vec);
    }

    public void subPos(Vector3f vec) {
        pos = pos.sub(vec);
    }

    public void addPos(float x, float y, float z) {
        this.addPos(new Vector3f(x,y,z));
    }

    public void subPos(float x, float y, float z) {
        this.subPos(new Vector3f(x,y,z));
    }

    public void setPos(Vector3f vec) {
        this.pos = vec;
    }

    public void setPos(float x, float y, float z) {
        this.pos = new Vector3f(x,y,z);
    }

    public void addRotation(Vector3f vec) {
        rotation = rotation.add(vec);
    }

    public void subRotation(Vector3f vec) {
        rotation = rotation.sub(vec);
    }

    public void addRotation(float x, float y, float z) {
        this.addRotation(new Vector3f(x,y,z));
    }

    public void subRotation(float x, float y, float z) {
        this.subRotation(new Vector3f(x,y,z));
    }

    public void setRotation(Vector3f vec) {
        this.rotation = vec;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x,y,z);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public Boolean isCullable() {
        return cullable;
    }

    public void setCullable(boolean cullable) {
        this.cullable = cullable;
    }
}
