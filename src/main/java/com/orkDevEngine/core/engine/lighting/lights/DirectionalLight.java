package com.orkDevEngine.core.engine.lighting.lights;

import org.joml.Vector3f;

public class DirectionalLight extends ILight{

    public DirectionalLight(Vector3f color, float intensity) {
        super(color, new Vector3f(0,1,1).normalize(), intensity);
    }

    public void rotateX(float angleDeg){
        this.position.rotateX((float)Math.toRadians(angleDeg));

    }
    public void rotateY(float angleDeg){
        this.position.rotateY((float)Math.toRadians(angleDeg));
    }
    public void rotateZ(float angleDeg){
        this.position.rotateZ((float)Math.toRadians(angleDeg));
    }

    @Override
    public void setPosition(Vector3f position) {
        super.setPosition(position);
        this.position.normalize();
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
        this.position.normalize();
    }
}
