package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

public class PlayerDialogSystem extends EntitySystem {
    // Player <-> NPC
    // Player get item
    // Time pass
    //...
    public PlayerDialogSystem(){
        Family dialogableNPC = Family.all().get();
        Family dialogableItem = Family.all().get();
    }
}
