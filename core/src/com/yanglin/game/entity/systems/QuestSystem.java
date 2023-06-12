package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class QuestSystem extends IteratingSystem implements PlayerInteractionSystem.PlayerInteractionListener {
    public QuestSystem() {
        super(Family.all().get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void triggerEvent(EventType eventType) {

    }
}
