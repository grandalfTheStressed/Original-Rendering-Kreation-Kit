package com.orkDevEngine.core.engine.screen.rendering.renderers;

import com.orkDevEngine.core.engine.game.objects.entity.BaseEntity;
import com.orkDevEngine.core.engine.game.objects.managers.EntityManager;
import com.orkDevEngine.core.engine.screen.shader.managers.ShaderManager;
import com.orkDevEngine.core.engine.screen.shader.managers.UniformManager;
import com.orkDevEngine.core.engine.utils.Transformation;
import com.orkDevEngine.core.engine.utils.Utilities;
import com.orkDevEngine.core.engine.utils.exceptions.RendererException;
import com.orkDevEngine.core.engine.utils.exceptions.UniformManagerException;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntityRenderer implements IRenderer{
    private EntityManager entityManager;

    public EntityRenderer(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void init() {
    }

    public boolean renderWithoutCull() {
        glDisable(GL_CULL_FACE);
        return false;
    }

    public boolean renderWithCull() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        return true;
    }

    @Override
    public void render(UniformManager uniformManager) {
        entityManager.getEntities().forEach(entity -> {
            boolean culling = entity.isCullable() ? renderWithCull() : renderWithoutCull();
            bindEntity(uniformManager, entity);
            glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindCurrentEntity();
        });
    }

    @Override
    public void bindEntity(UniformManager uniformManager, BaseEntity entity) {
        Matrix4f translate = Transformation.createTranslationMatrix(entity);
        Matrix4f rotate = Transformation.createRotationMatrix(entity);
        Matrix4f scale = Transformation.createScaleMatrix(entity);

        uniformManager.setUniform("textureSampler", 0);
        uniformManager.setUniform("rotationMatrix", rotate);
        uniformManager.setUniform("transformationMatrix", Transformation.createTransformationMatrix(translate, rotate, scale));
        uniformManager.setUniform("material", entity.getModel().getMaterial());
        glBindVertexArray(entity.getModel().getId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        if(entity.getModel().getMaterial().hasTexture()) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, entity.getModel().getTexture().getId());
        }
    }

    @Override
    public void unbindCurrentEntity() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
