package com.orkDevEngine.core.engine.games;

import com.orkDevEngine.core.engine.game.objects.managers.EntityManager;
import com.orkDevEngine.core.engine.screen.input.KeyboardManager;
import com.orkDevEngine.core.engine.screen.input.MouseManager;

public interface IGameLogic {

    void init(MouseManager mouseManager, KeyboardManager keyboardManager) throws Exception;

    EntityManager getEntities();
    void input();

    void update(float interval);

    void render();

    void cleanUp();
}
