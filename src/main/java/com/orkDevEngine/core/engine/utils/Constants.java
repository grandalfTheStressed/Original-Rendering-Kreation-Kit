package com.orkDevEngine.core.engine.utils;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Constants {

    public static final String TITLE = "Ork Dev Engine";

    public static final long NANO_SECOND = 1000000000l;
    public static final long FRAME_RATE = 60l;
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;
    public static final float FOV = Math.toRadians(75);
    public static final float MOUSE_SENSITIVIY = 0.2f;
    public static final float SPECULAR_POWER = 10f;

    public static Vector4f DEFAULT_COLOR = new Vector4f(.3f,.3f,.3f,0f);

    public static final Vector3f AMBIENT_LIGHT = new Vector3f(.2f,.2f,.2f);

}
