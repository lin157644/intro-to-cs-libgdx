package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.yanglin.game.GameAssetManager;

public class PlayerInteractionSystem extends EntitySystem {
    // Player <-> Item
    // Use tiled object layer
    public void PlayerInteractionListener(GameAssetManager assetManager){
        // assetManager.get("tilemap");
    }
    public interface PlayerInteractionListener {

    }
}
