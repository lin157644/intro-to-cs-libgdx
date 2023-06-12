package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.*;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;

public class PlayerMovementSystem extends IteratingSystem implements KeyInputListener, MapManager.MapListener, TimeSystem.TimeSystemListener {
    private static final String TAG = Game.class.getSimpleName();
    private final OrthographicCamera camera;
    private FacingComponent.Facing currentFacing = FacingComponent.Facing.FRONT;
    private FacingComponent.Facing previousFacing = FacingComponent.Facing.FRONT;
    private boolean currentIsWalking = false;
    private boolean previousIsWalking = false;
    private GameAssetManager assetManager;
    private MapManager mapManager;
    private TiledMap map;
    private TiledMapTileLayer collisionLayer;
    private final Vector2 velocity = new Vector2();
    private final Vector2 accleration = new Vector2(0.1f, 0.1f);
    private Vector2 cameraPos = new Vector2();

    // private final ComponentMapper<AnimationComponent> am;

    public PlayerMovementSystem(GameAssetManager assetManager, MapManager mapManager, OrthographicCamera camera) {
        super(Family.all(PlayerComponent.class, PositionComponent.class, AnimationComponent.class).get());
        // am = ComponentMapper.getFor(AnimationComponent.class);
        this.camera = camera;
        this.assetManager = assetManager;
        this.mapManager = mapManager;

        map = assetManager.get(mapManager.getCurrentMap().getFileName());
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
        mapManager.addMapListener(this);

        Gdx.app.debug(TAG, "Successfully initialized PlayerSystem");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // final PlayerComponent playerComponent = EntityEngine.playerComponentMapper.get(entity);
        final PositionComponent positionComponent = EntityEngine.positionComponentMapper.get(entity);
        final StateComponent stateComponent = EntityEngine.stateComponentMapper.get(entity);
        if (!currentFacing.equals(previousFacing) || currentIsWalking != previousIsWalking) {
            if (currentIsWalking) {
                switch (currentFacing) {
                    case FRONT -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.WALK_FRONT.getType());
                    }
                    case BACK -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.WALK_BACK.getType());
                    }
                    case LEFT -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.WALK_LEFT.getType());
                    }
                    case RIGHT -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.WALK_RIGHT.getType());
                    }
                }
            } else {
                switch (currentFacing) {
                    case FRONT -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.IDLE_FRONT.getType());
                    }
                    case BACK -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.IDLE_BACK.getType());
                    }
                    case LEFT -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.IDLE_LEFT.getType());
                    }
                    case RIGHT -> {
                        stateComponent.set(GameAssetManager.PlayerAnimation.IDLE_RIGHT.getType());
                    }
                }
            }
            previousFacing = currentFacing;
            previousIsWalking = currentIsWalking;
        }

        // TODO: Improve performance?

        boolean collisionX = false, collisionY = false;

        float targetX = positionComponent.position.x + velocity.x;
        float targetY = positionComponent.position.y + velocity.y;

        // Origin (0, 0) at bottom left
        if (velocity.x < 0) {
            // Bottom left
            collisionX = collisionLayer.getCell((int) (targetX), (int) (targetY)) != null;
        } else if (velocity.x > 0) {
            // Bottom right
            collisionX = collisionLayer.getCell((int) (targetX + 1), (int) (targetY)) != null;
        }

        if (velocity.y < 0) {
            // Bottom left
            collisionY = collisionLayer.getCell((int) (targetX), (int) (targetY)) != null;
        } else if (velocity.y > 0) {
            // Bottom right
            collisionY = collisionLayer.getCell((int) (targetX + 1), (int) (targetY)) != null;
        }

        if (!collisionX) positionComponent.position.x += velocity.x;
        if (!collisionY) positionComponent.position.y += velocity.y;

        cameraPos.set(entity.getComponent(PositionComponent.class).position);

        // TODO: Deal with samll tilemap
        float xmax = map.getProperties().get("width", Integer.class);
        float ymax = map.getProperties().get("height", Integer.class);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float scaledViewportWidthHalfExtent = (w / h) * 20 * 0.5f;
        float scaledViewportHeightHalfExtent = 20 * 0.5f;
        // Horizontal
        if (cameraPos.x < scaledViewportWidthHalfExtent)
            cameraPos.x = scaledViewportWidthHalfExtent;
        else if (cameraPos.x > xmax - scaledViewportWidthHalfExtent)
            cameraPos.x = xmax - scaledViewportWidthHalfExtent;

        // Vertical
        if (cameraPos.y < scaledViewportHeightHalfExtent)
            cameraPos.y = scaledViewportHeightHalfExtent;
        else if (cameraPos.y > ymax - scaledViewportHeightHalfExtent)
            cameraPos.y = ymax - scaledViewportHeightHalfExtent;

        camera.position.set(cameraPos, 0);
        camera.update();
        // Gdx.app.debug("PlayerSystem", "PlayerX:" + positionComponent.position.y + "PlayerY:" + positionComponent.position.y);
    }

    private boolean isWalking(GameInputProcessor gameInputProcessor) {
        if (gameInputProcessor.isKeyDown(Input.Keys.LEFT)
                || gameInputProcessor.isKeyDown(Input.Keys.RIGHT)
                || gameInputProcessor.isKeyDown(Input.Keys.UP)
                || gameInputProcessor.isKeyDown(Input.Keys.DOWN)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        // TODO: Refactor moving logic
        switch (keycode) {
            case Input.Keys.RIGHT -> {
                velocity.x = accleration.x;
                currentFacing = FacingComponent.Facing.RIGHT;
            }
            case Input.Keys.LEFT -> {
                velocity.x = -accleration.x;
                currentFacing = FacingComponent.Facing.LEFT;
            }
            case Input.Keys.DOWN -> {
                velocity.y = -accleration.y;
                currentFacing = FacingComponent.Facing.FRONT;
            }
            case Input.Keys.UP -> {
                velocity.y = accleration.y;
                currentFacing = FacingComponent.Facing.BACK;
            }
        }
        currentIsWalking = isWalking(gameInputProcessor);
        return true;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.LEFT)) {
                    velocity.x = -accleration.x;
                    currentFacing = FacingComponent.Facing.LEFT;
                } else {
                    velocity.x = 0f;
                    if (velocity.y > 0) {
                        currentFacing = FacingComponent.Facing.BACK;
                    } else if (velocity.y < 0) {
                        currentFacing = FacingComponent.Facing.FRONT;
                    }
                }
            }
            case Input.Keys.LEFT -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.RIGHT)) {
                    velocity.x = accleration.x;
                    currentFacing = FacingComponent.Facing.RIGHT;
                } else {
                    velocity.x = 0f;
                    if (velocity.y > 0) {
                        currentFacing = FacingComponent.Facing.BACK;
                    } else if (velocity.y < 0) {
                        currentFacing = FacingComponent.Facing.FRONT;
                    }
                }
            }
            case Input.Keys.DOWN -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.UP)) {
                    velocity.y = accleration.y;
                    currentFacing = FacingComponent.Facing.BACK;
                } else {
                    velocity.y = 0f;
                    if (velocity.x > 0) {
                        currentFacing = FacingComponent.Facing.RIGHT;
                    } else if (velocity.x < 0) {
                        currentFacing = FacingComponent.Facing.LEFT;
                    }
                }
            }
            case Input.Keys.UP -> {
                if (gameInputProcessor.isKeyDown(Input.Keys.UP)) {
                    velocity.y = accleration.y;
                    currentFacing = FacingComponent.Facing.FRONT;
                } else {
                    velocity.y = 0f;
                    if (velocity.x > 0) {
                        currentFacing = FacingComponent.Facing.RIGHT;
                    } else if (velocity.x < 0) {
                        currentFacing = FacingComponent.Facing.LEFT;
                    }
                }
            }
        }
        currentIsWalking = isWalking(gameInputProcessor);
        return true;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }

    @Override
    public void mapChanged(MapManager.EMap EMap) {
        // Change collision layer
        map = assetManager.get(mapManager.getCurrentMap().getFileName());
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");

        // Reset player position
        // TODO: Different position depends on source
        PositionComponent pos = EntityEngine.positionComponentMapper.get(getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first());
        pos.position.x = map.getProperties().get("spawnX", Integer.class);
        pos.position.y = map.getProperties().get("spawnY", Integer.class);

    }

    @Override
    public void onMonthUpdate(int month) {
        if(month==6) {
            accleration.set(0.2f, 0.2f);
        } else {
            accleration.set(0.1f, 0.1f);
        }
    }

    @Override
    public void onDayUpdate(int day) {

    }
}
