package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.yanglin.game.entity.component.CollisionComponent;

public class CollisionSystem extends IteratingSystem {
    public CollisionSystem () {
        super(Family.all(CollisionComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
