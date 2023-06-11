package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.yanglin.game.entity.component.ItemComponent;
import com.yanglin.game.entity.component.TextureComponent;

public class InventorySystem extends IteratingSystem {
    public InventorySystem(){
        super(Family.all(TextureComponent.class, ItemComponent.class).get());
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO:
    }
}
