package com.orkDevEngine.core.engine.screen.rendering.managers;

import com.orkDevEngine.core.engine.screen.cameras.Camera;

public interface IRenderManager {

    void init() throws Exception;

    void prepareView(Camera camera);

    void render(Camera camera);

    void clear();

    void cleanUp();
}
