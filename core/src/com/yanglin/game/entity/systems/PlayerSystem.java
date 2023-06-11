package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.yanglin.game.MainAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.component.*;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;

public class PlayerSystem extends IteratingSystem implements KeyInputListener {
    private final OrthographicCamera camera;
    private FacingComponent.Facing currentFacing = FacingComponent.Facing.FRONT;
    private FacingComponent.Facing previousFacing = FacingComponent.Facing.FRONT;
    private boolean currentIsWalking = false;
    private boolean previousIsWalking = false;
    private MainAssetManager assetManager;

    // private final ComponentMapper<AnimationComponent> am;

    public PlayerSystem(OrthographicCamera camera, MainAssetManager assetManager) {
        super(Family.all(PlayerComponent.class, PositionComponent.class, AnimationComponent.class).get());
        // am = ComponentMapper.getFor(AnimationComponent.class);
        this.camera = camera;
        this.assetManager = assetManager;
    }

    private final Vector2 velocity = new Vector2();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // final PlayerComponent playerComponent = EntityEngine.playerComponentMapper.get(entity);
        final PositionComponent positionComponent = EntityEngine.positionComponentMapper.get(entity);
        final StateComponent stateComponent = EntityEngine.stateComponentMapper.get(entity);
        if (!currentFacing.equals(previousFacing) || currentIsWalking != previousIsWalking) {
            if (currentIsWalking) {
                switch (currentFacing) {
                    case FRONT -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.WALK_FRONT.getType());
                    }
                    case BACK -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.WALK_BACK.getType());
                    }
                    case LEFT -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.WALK_LEFT.getType());
                    }
                    case RIGHT -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.WALK_RIGHT.getType());
                    }
                }
            } else {
                switch (currentFacing) {
                    case FRONT -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.IDLE_FRONT.getType());
                    }
                    case BACK -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.IDLE_BACK.getType());
                    }
                    case LEFT -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.IDLE_LEFT.getType());
                    }
                    case RIGHT -> {
                        stateComponent.set(MainAssetManager.PlayerAnimation.IDLE_RIGHT.getType());
                    }
                }
            }
            previousFacing = currentFacing;
            previousIsWalking = currentIsWalking;
        }

        // TODO: Do collision
        TiledMap map = assetManager.get("tilemaps/school_gate.tmx");
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");

        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        float targetX =  positionComponent.position.x + velocity.x;
        float targetY =  positionComponent.position.y + velocity.y;
        // Origin (0, 0) at bottom left
        if (velocity.x < 0) {
            // Bottom left
            collisionX = collisionLayer.getCell((int) (targetX), (int) (targetY)) != null;
            // Top left
            if (!collisionX)
                collisionX = collisionLayer.getCell((int) (targetX), (int) (targetY + 1)) != null;
        } else if (velocity.x > 0) {
            // Bottom right
            collisionX = collisionLayer.getCell((int) (targetX + 1), (int) (targetY)) != null;
            // Top right
            if (!collisionX)
                collisionX = collisionLayer.getCell((int) (targetX + 1), (int) (targetY + 1)) != null;
        }

        if (velocity.y < 0) {
            // Bottom left
            collisionY = collisionLayer.getCell((int) (targetX), (int) (targetY)) != null;
            // Top left
            if (!collisionY)
                collisionY = collisionLayer.getCell((int) (targetX), (int) (targetY + 1)) != null;
        } else if (velocity.y > 0) {
            // Bottom right
            collisionY = collisionLayer.getCell((int) (targetX + 1), (int) (targetY)) != null;
            // Top right
            if (!collisionY)
                collisionY = collisionLayer.getCell((int) (targetX + 1), (int) (targetY + 1)) != null;
        }

        if (!collisionX)
            positionComponent.position.x += velocity.x;
        if (!collisionY)
            positionComponent.position.y += velocity.y;

        camera.position.set(entity.getComponent(PositionComponent.class).position, 0);
        // TODO: Deal with camera out of bound

        camera.update();
        // Gdx.app.debug("PlayerSystem", "PlayerX:" + positionComponent.position.y + "PlayerY:" + positionComponent.position.y);
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT -> {
                velocity.x = 0.1f;
                currentFacing = FacingComponent.Facing.RIGHT;
                currentIsWalking = true;
            }
            case Input.Keys.LEFT -> {
                velocity.x = -0.1f;
                currentFacing = FacingComponent.Facing.LEFT;
                currentIsWalking = true;
            }
            case Input.Keys.DOWN -> {
                velocity.y = -0.1f;
                currentFacing = FacingComponent.Facing.FRONT;
                currentIsWalking = true;
            }
            case Input.Keys.UP -> {
                velocity.y = 0.1f;
                currentFacing = FacingComponent.Facing.BACK;
                currentIsWalking = true;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.LEFT)) {
                    velocity.x = -0.1f;
                } else {
                    velocity.x = 0f;
                    currentIsWalking = false;
                }
            }
            case Input.Keys.LEFT -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.RIGHT)) {
                    velocity.x = 0.1f;
                } else {
                    velocity.x = 0f;
                    currentIsWalking = false;
                }
            }
            case Input.Keys.DOWN -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.UP)) {
                    velocity.y = 0.1f;
                } else {
                    velocity.y = 0f;
                    currentIsWalking = false;
                }
            }
            case Input.Keys.UP -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.UP)) {
                    velocity.y = -0.1f;
                } else {
                    velocity.y = 0f;
                    currentIsWalking = false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }
}
