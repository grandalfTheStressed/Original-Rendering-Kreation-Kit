package com.orkDevEngine.core.engine.game.objects.entity;

import com.orkDevEngine.core.engine.game.objects.Material;
import com.orkDevEngine.core.engine.game.objects.Model;
import com.orkDevEngine.core.engine.utils.ObjectLoader;
import org.joml.Vector3f;

public class TerrainEntity extends BaseEntity{

    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    public TerrainEntity(ObjectLoader objectLoader, Vector3f pos, Vector3f rotation, Material material, boolean cullable) {
        super(null, pos, rotation, 1, true);
        this.setModel(generateTerrain(objectLoader));
    }

    private Model generateTerrain(ObjectLoader objectLoader) {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] verts = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] texture = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;

        for(int i = 0; i < VERTEX_COUNT; i++)
            for (int j = 0; j < VERTEX_COUNT; j++) {
                verts[vertexPointer * 3] = j / (VERTEX_COUNT - 1.0f) * SIZE;
                verts[vertexPointer * 3 + 1] = 0; //HeightMap
                verts[vertexPointer * 3 + 2] = i / (VERTEX_COUNT - 1.0f) * SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                texture[vertexPointer * 2] = j / (VERTEX_COUNT - 1.0f) * SIZE;
                texture[vertexPointer * 2 + 1] = i / (VERTEX_COUNT - 1.0f) * SIZE;
                vertexPointer++;
            }

        int pointer = 0;
        return null;
    }

    @Override
    public void setScale(float scale) {
        return;
    }
}
