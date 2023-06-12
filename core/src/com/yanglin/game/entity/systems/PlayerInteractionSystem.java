package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.MapManager;

public class PlayerInteractionSystem extends EntitySystem implements MapManager.MapListener {
    // Player <-> Item
    // Use tiled object layer

    public void PlayerInteractionListener(GameAssetManager assetManager, MapManager mapManager){
        assetManager.get("tilemap/");
        mapManager.addMapListener(this);
    }

    @Override
    public void mapChanged(MapManager.EMap EMap) {

    }

    public interface PlayerInteractionListener {

    }
}
