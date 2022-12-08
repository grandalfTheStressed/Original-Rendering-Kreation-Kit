package com.orkDevEngine.core.engine.screen.rendering.renderers;

import com.orkDevEngine.core.engine.game.objects.entity.BaseEntity;
import com.orkDevEngine.core.engine.lighting.LightManager;
import com.orkDevEngine.core.engine.screen.shader.managers.ShaderManager;
import com.orkDevEngine.core.engine.screen.shader.managers.UniformManager;
import com.orkDevEngine.core.engine.utils.exceptions.RendererException;
import org.joml.Matrix4f;

public interface IRenderer {

    void init();
    void bindEntity(UniformManager uniformManager, BaseEntity entity);

    void render(UniformManager uniformManager);
    void unbindCurrentEntity();

    void cleanUp();
}
