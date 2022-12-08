package com.orkDevEngine.core.engine.games;

import com.orkDevEngine.core.engine.game.objects.managers.EntityManager;
import com.orkDevEngine.core.engine.game.objects.managers.GameObjectEntityManager;
import com.orkDevEngine.core.engine.lighting.LightManager;
import com.orkDevEngine.core.engine.screen.cameras.EditCamera;
import com.orkDevEngine.core.engine.screen.input.KeyboardManager;
import com.orkDevEngine.core.engine.screen.input.MouseManager;
import com.orkDevEngine.core.engine.screen.WindowManager;
import com.orkDevEngine.core.engine.screen.rendering.managers.RenderManager;
import com.orkDevEngine.launcher.Main;

public class TestGame implements IGameLogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderManager;

    private final GameObjectEntityManager gameObjectManager;
    private final WindowManager window;

    private LightManager lightManager;
    private EditCamera editCamera;

    MouseManager mouseManager;

    KeyboardManager keyboardManager;
    private boolean lightCycle = false;

    public TestGame() {
        window = Main.getWindow();
        gameObjectManager = new GameObjectEntityManager();
        lightManager = new LightManager();
        renderManager = new RenderManager(lightManager, gameObjectManager);
    }

    @Override
    public void init(MouseManager mouseManager, KeyboardManager keyboardManager) throws Exception {
        this.keyboardManager = keyboardManager;
        this.mouseManager = mouseManager;
        editCamera = new EditCamera(keyboardManager, mouseManager);
        gameObjectManager.init();
        lightManager.init();
        renderManager.init();
    }

    @Override
    public EntityManager getEntities() {
        return gameObjectManager;
    }

    @Override
    public void input() {

        keyboardManager.input();
        mouseManager.input();
    }

    @Override
    public void update(float interval) {

        editCamera.update(interval);
        lightManager.update();
        //gameObjectManager.getEntities().get(0).addRotation(new Vector3f(0, .1f, 0));
    }

    @Override
    public void render() {
        renderManager.render(editCamera);
    }

    @Override
    public void cleanUp() {
        renderManager.cleanUp();
        gameObjectManager.cleanUp();
    }
}
