package com.orkDevEngine.core.engine.game.objects.managers;

import com.orkDevEngine.core.engine.game.objects.Model;
import com.orkDevEngine.core.engine.game.objects.Texture;
import com.orkDevEngine.core.engine.game.objects.entity.Actor;
import org.joml.Vector3f;

public class ActorManager extends EntityManager {

    Actor entity0;
    Actor entity1;

    Actor entity2;
    public ActorManager() {
        super();
    }

    @Override
    public void init() throws Exception {
        Model model = objectLoader.loadOBJModel("/models/monkey.obj");
        model.setTexture(new Texture(objectLoader.loadTexture("textures/bricks.jpg")), 1.1f);
        entity0 = new Actor(model, new Vector3f(0,0,-15f), new Vector3f(0,0,0), 1, true);

        Model model2 = objectLoader.loadOBJModel("/models/teapot2.obj");
        model2.setTexture(new Texture(objectLoader.loadTexture("textures/grassblock.png")), 1f);
        entity1 = new Actor(model2, new Vector3f(-10.5f,-1.5f,-7.5f), new Vector3f(30f,-50f,0), 1, false);

        Model model3 = objectLoader.loadOBJModel("/models/quadCube.obj");
        model3.setTexture(new Texture(objectLoader.loadTexture("textures/grassblock.png")), 1f);
        entity2 = new Actor(model3, new Vector3f(-5.5f,-1.5f,-7.5f), new Vector3f(30f,-50f,0), 1, true);

        entities.add(entity0);
        entities.add(entity1);
        entities.add(entity2);
    }
}
