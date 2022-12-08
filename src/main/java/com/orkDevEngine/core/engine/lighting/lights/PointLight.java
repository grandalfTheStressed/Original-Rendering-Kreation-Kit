package com.orkDevEngine.core.engine.lighting.lights;

import org.joml.Vector3f;

public class PointLight extends ILight{
    private float intensity, constant, linear, exponent;

    public PointLight(Vector3f color, Vector3f position, float intensity, float constant, float linear, float exponent) {
        super(color, position, intensity);
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
    }

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        this(color, position, intensity, 1, 0, 0);
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getExponent() {
        return exponent;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }
}
