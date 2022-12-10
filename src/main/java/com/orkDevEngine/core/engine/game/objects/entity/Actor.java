package com.orkDevEngine.core.engine.game.objects.entity;

import com.orkDevEngine.core.engine.game.objects.Model;
import org.joml.Vector3f;

public class Actor extends BaseEntity{
    public Actor(Model model, Vector3f pos, Vector3f rotation, float scale, boolean cullable) {
        super(model, pos, rotation, scale, cullable);
    }
}
