package com.orkDevEngine.core.engine.game.objects.managers;

import com.orkDevEngine.core.engine.game.objects.Model;
import com.orkDevEngine.core.engine.game.objects.Texture;
import com.orkDevEngine.core.engine.game.objects.entity.GameObjectEntity;
import org.joml.Vector3f;

public class GameObjectEntityManager extends EntityManager {

    GameObjectEntity entity0;
    GameObjectEntity entity1;
    public GameObjectEntityManager() {
        super();
    }

    @Override
    public void init() throws Exception {
        Model model = objectLoader.loadOBJModel("/models/monkey.obj");
        model.setTexture(new Texture(objectLoader.loadTexture("textures/bricks.jpg")), 1.1f);
        entity0 = new GameObjectEntity(model, new Vector3f(0,0,-15f), new Vector3f(0,0,0), 1, true);

        Model model2 = objectLoader.loadOBJModel("/models/teapotTest.obj");
        model2.setTexture(new Texture(objectLoader.loadTexture("textures/grassblock.png")), 1f);
        entity1 = new GameObjectEntity(model2, new Vector3f(-10.5f,-1.5f,-7.5f), new Vector3f(30f,-50f,0), 1, false);

        entities.add(entity0);
        entities.add(entity1);
    }
}
