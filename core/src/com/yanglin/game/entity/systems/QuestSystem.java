package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class QuestSystem extends IteratingSystem {
    public QuestSystem() {
        super(Family.all().get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
