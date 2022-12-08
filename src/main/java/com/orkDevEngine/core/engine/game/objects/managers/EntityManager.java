package com.orkDevEngine.core.engine.game.objects.managers;

import com.orkDevEngine.core.engine.game.objects.entity.BaseEntity;
import com.orkDevEngine.core.engine.utils.ObjectLoader;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityManager {

    protected final static ObjectLoader objectLoader = new ObjectLoader();
    protected List<BaseEntity> entities;

    public EntityManager() {
        entities = new ArrayList<>();
    }
    public abstract void init() throws Exception;

    public List<BaseEntity> getEntities() {
        return entities;
    }

    public void cleanUp() {
        objectLoader.cleanUp();
    }
}
