package com.orkDevEngine.core.engine.utils;

import com.orkDevEngine.core.engine.game.objects.entity.BaseEntity;
import com.orkDevEngine.core.engine.screen.cameras.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Matrix4f translate, Matrix4f rotate, Matrix4f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix
                .identity()
                .mul(translate)
                .mul(rotate)
                .mul(scale);
        return matrix;
    }

    public static Matrix4f createTranslationMatrix(BaseEntity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix
                .identity()
                .translate(entity.getPosition());
        return matrix;
    }

    public static Matrix4f createRotationMatrix(BaseEntity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix
                .identity()
                .rotateX((float)Math.toRadians(entity.getRotation().x))
                .rotateY((float)Math.toRadians(entity.getRotation().y))
                .rotateZ((float)Math.toRadians(entity.getRotation().z));
        return matrix;
    }

    public static Matrix4f createScaleMatrix(BaseEntity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix
                .identity()
                .scale(entity.getScale());
        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPos();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix
                .identity()
                .rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0 ))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0 ))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1 ))
                .translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }

    public static Matrix4f getLookAtMatrix(Matrix4f eyeTransformMatrix, Vector3f eye, Vector3f lookAt) {

        Vector4f upVector = new Vector4f(0,1,0,0).mul(eyeTransformMatrix);

        Vector3f zaxis = lookAt.sub(eye).normalize();
        Vector3f xaxis = zaxis.cross(new Vector3f(upVector.x,upVector.y, upVector.z)).normalize();
        Vector3f yaxis = xaxis.cross(zaxis);

        Matrix4f matrix = new Matrix4f();

        Vector4f c1 = new Vector4f(xaxis.x, yaxis.x, zaxis.x, 0);
        Vector4f c2 = new Vector4f(xaxis.y, yaxis.y, zaxis.y, 0);
        Vector4f c3 = new Vector4f(xaxis.z, yaxis.z, zaxis.z, 0);
        Vector4f c4 = new Vector4f(-xaxis.dot(eye), -yaxis.dot(eye), -zaxis.dot(eye), 1);
        matrix.set(c1,c2,c3,c4);

        return matrix;
    }
}
