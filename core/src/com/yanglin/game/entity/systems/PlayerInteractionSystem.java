package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.GameState;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.ItemComponent;
import com.yanglin.game.entity.component.PlayerComponent;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;

public class PlayerInteractionSystem extends IteratingSystem implements MapManager.MapListener, KeyInputListener {
    // Player <-> Item
    // Use tiled object layer
    private static final String TAG = PlayerInteractionSystem.class.getSimpleName();
    private TiledMapTileLayer objectLayer;
    private MapManager mapManager;
    private GameAssetManager assetManager;
    private GameState gameState;
    private final Array<PlayerInteractionListener> playerInteractionListeners = new Array<>();

    public PlayerInteractionSystem(GameAssetManager assetManager, MapManager mapManager, GameState gameState) {
        super(Family.all(PlayerComponent.class, PositionComponent.class).get());

        this.assetManager = assetManager;
        this.mapManager = mapManager;
        this.gameState = gameState;
        mapManager.addMapListener(this);

        TiledMap tiledMap = assetManager.get(mapManager.getCurrentMap().getFileName());
        objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("obj");
    }

    @Override
    public void mapChanged(MapManager.EMap EMap) {
        TiledMap tiledMap = assetManager.get(EMap.getFileName());
        objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("obj");
    }

    private TiledMapTile collideWithObject(float x, float y) {
        TiledMapTile tile = null;
        TiledMapTileLayer.Cell cell;
        if ((cell = objectLayer.getCell((int) (x), (int) (y))) == null
                && (cell = objectLayer.getCell((int) (x), (int) (y + 0.5))) == null
                && (cell = objectLayer.getCell((int) (x + 0.5), (int) (y))) == null
                && (cell = objectLayer.getCell((int) (x + 0.5), (int) (y + 0.5))) == null) {
            return null;
        }
        if ((tile = cell.getTile()).getProperties().containsKey("type"))
            return tile;
        return null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = EntityEngine.positionComponentMapper.get(entity);
        // Do player collide with object?
        TiledMapTile tile;
        if ((tile = collideWithObject(positionComponent.position.x, positionComponent.position.y)) != null) {
            // What do player collide
            String type = tile.getProperties().get("type", String.class);
            switch (type) {
                // Collide with map changing area
                case "MAP" -> {
                    String mapDest = tile.getProperties().get("dest", String.class);
                    if (mapDest != null) {
                        Gdx.app.debug(TAG, "Detect collision of map type, Dest: " + mapDest);
                        mapManager.setCurrentMap(MapManager.EMap.valueOf(mapDest));
                    } else {
                        Gdx.app.log(TAG, "Detect map destination of null");
                    }
                }
                // Collide with event (e.g. bus)
                case "EVENT" -> {
                    String eventType = tile.getProperties().get("event", String.class);
                    switch (eventType) {
                        case "BUS" -> {
                            // TODO: Play animation
                            // TODO: To bad end
                        }
                        case "LANGUAGE" -> {
                            // TODO: Trigger QuestSystem

                        }
                        case "GIFT2PROF" -> {
                            // TODO: Trigger QuestSystem
                        }
                        default -> {
                            Gdx.app.log(TAG, "Detect item of unexpected type");
                        }
                    }
                }
                case "ITEM", "DIALOG" -> {}
                default -> Gdx.app.log(TAG, "Detect collision of unexpected type");
            }
        }
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        switch (keycode) {
            case Input.Keys.Z -> {
                Entity entity = getEngine().getEntitiesFor(getFamily()).first();
                PositionComponent positionComponent = EntityEngine.positionComponentMapper.get(entity);
                // Do player collide with object?
                TiledMapTile tile;
                if ((tile = collideWithObject(positionComponent.position.x, positionComponent.position.y)) != null) {
                    // What do player collide
                    String type = tile.getProperties().get("type", String.class);
                    switch (type) {
                        // Collide with pick-up-able item
                        case "ITEM" -> {
                            String itemType = tile.getProperties().get("item", String.class);
                            switch (itemType) {
                                case "APPLE", "BOOK", "ENGLISH", "WALLET" -> {
                                    // Pickup the apple under the tree in school gate
                                    // Pickup the book in dorm room
                                    // Get english transcript in dorm room
                                    // Get wallet in dorm room
                                    gameState.addItem(ItemComponent.ItemType.valueOf(itemType));
                                    Gdx.app.log(TAG, "Item \"" + itemType + "\" added to gameState");
                                    // TODO: Trigger Dialog
                                }
                                default -> Gdx.app.log(TAG, "Detect item of unexpected type");
                            }
                        }
                        // Collide with dialogable area
                        case "DIALOG" -> {
                            String dialogType = tile.getProperties().get("dialog", String.class);
                            switch (dialogType) {
                                case "LIBRARIAN" -> {
                                    // Interact with librarian
                                    // TODO: Trigger dialog
                                }
                                case "COMPUTER" -> {
                                    // 線上畢業審核
                                    // TODO: Trigger dialog
                                }
                                case "PROHIBIT" -> {
                                    // 拿時數
                                    // TODO: Trigger dialog
                                }
                                default -> Gdx.app.log(TAG, "Detect dialog of unexpected type");
                            }
                        }
                        case "MAP", "EVENT" -> {}
                        default -> Gdx.app.log(TAG, "Detect collision of unexpected type");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }

    public void addPlayerInteractionListener(PlayerInteractionListener listener) {
        playerInteractionListeners.add(listener);
    }

    private void notifyListeners(PlayerInteractionListener.EventType eventType) {
        for (PlayerInteractionListener listener : playerInteractionListeners) {
            listener.triggerEvent(eventType);
        }
    }

    public interface PlayerInteractionListener {
        enum EventType {
            ENTER_LANGUAGE_CENTER,
            GIFT2PROF,
        }
        // Quest, Item, Dialog
        void triggerEvent(EventType eventType);
    }
}
