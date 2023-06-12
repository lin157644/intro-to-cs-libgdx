package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.PlayerComponent;
import com.yanglin.game.entity.component.PositionComponent;

public class PlayerInteractionSystem extends IteratingSystem implements MapManager.MapListener {
    // Player <-> Item
    // Use tiled object layer

    private MapObjects objects;
    private Family family;
    private MapManager mapManager;
    public PlayerInteractionSystem(GameAssetManager assetManager, MapManager mapManager){
        super(Family.all(PlayerComponent.class, PositionComponent.class).get());
        this.mapManager = mapManager;
        TiledMap tiledMap = assetManager.get(mapManager.getCurrentMap().getFileName());
        objects = tiledMap.getLayers().get("obj").getObjects();
        for (MapObject mapObject : objects) {
            System.out.println(mapObject.getName());
        }
        mapManager.addMapListener(this);
    }

    @Override
    public void mapChanged(MapManager.EMap EMap) {

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = EntityEngine.positionComponentMapper.get(entity);
        // TODO: Detect collide with object?

        // TODO: Collide with map changing
        // mapManager.setCurrentMap(MapManager.EMap.DORM_INTERNAL);

        // TODO: Collide with event

        // TODO: Collide with
    }

    public interface PlayerInteractionListener {

    }
}
