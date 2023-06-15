package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.ItemComponent;
import com.yanglin.game.entity.component.PlayerComponent;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.Ending;

public class PlayerInteractionSystem extends IteratingSystem implements MapManager.MapListener, KeyInputListener {
    // Player <-> Item
    // Use tiled object layer
    private static final String TAG = PlayerInteractionSystem.class.getSimpleName();
    private TiledMapTileLayer objectLayer;
    private MapManager mapManager;
    private GameAssetManager assetManager;
    private GameState gameState;
    private IWantToGraduate game;
    private final Array<PlayerInteractionListener> playerInteractionListeners = new Array<>();

    private boolean hasTriggeredDialog = false;

    public PlayerInteractionSystem(IWantToGraduate game) {
        super(Family.all(PlayerComponent.class, PositionComponent.class).get());

        this.game = game;
        this.assetManager = game.assetManager;
        this.mapManager = game.mapManager;
        this.gameState = game.gameState;
        mapManager.addMapListener(this);

        TiledMap tiledMap = assetManager.get(mapManager.getCurrentMap().getFileName());
        objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("obj");
    }

    @Override
    public void mapChanged(MapManager.EMap EMap, float x, float y) {
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
                    float x = tile.getProperties().get("x", Float.class);
                    float y = tile.getProperties().get("y", Float.class);
                    if (mapDest != null) {
                        Gdx.app.debug(TAG, "Detect collision of map type, Dest: " + mapDest);
                        mapManager.setCurrentMap(MapManager.EMap.valueOf(mapDest), x, y);
                    } else {
                        Gdx.app.log(TAG, "Detect map destination of null");
                    }
                }
                // Collide with event (e.g. bus)
                case "EVENT" -> {
                    if(hasTriggeredDialog) break;
                    String eventType = tile.getProperties().get("event", String.class);
                    switch (eventType) {
                        case "SHOP" -> {
                            hasTriggeredDialog = true;
                            if(!gameState.hasItem(ItemComponent.ItemType.WALLET)) {
                                game.ending = Ending.AWKWARD_STORE;
                                game.changeScreen(EScreen.BAD_END);
                            } else {
                                gameState.hasEaten = true;
                            }
                        }
                        case "BUS" -> {
                            hasTriggeredDialog = true;
                            // TODO: Play animation
                            // TODO: To bad end
                            game.ending = Ending.CAR_CRUSH;
                            game.changeScreen(EScreen.BAD_END);
                        }
                        case "GIVE_ENGLISH" -> {
                            hasTriggeredDialog = true;
                            if (!gameState.hasItem(ItemComponent.ItemType.ENGLISH)) {
                                String text = "你連成績單都沒帶就想要畢業，回去宿舍找找吧！";
                                notifyDialogListeners(text);

                            } else if (!gameState.hasWorshiped) {
                                // Fail text won't be use here
                                game.ending = Ending.LOW_ENG_SCORE;
                                game.changeScreen(EScreen.BAD_END);
                            } else {
                                String text = "你顫抖得將多益成績單交給了語言中心服務人員 {NEXT} 在填寫了資料後，申請通過畢業門檻。";
                                notifyDialogListeners(text);
                            }

                        }
                        case "GIFT_PROF" -> {
                            hasTriggeredDialog = true;
                            if (gameState.hasItem(ItemComponent.ItemType.APPLE)) {
                                String text = "";
                                // TODO: Trigger　Dialog
                            } else {
                                // Fail text won't be use here
                                game.ending = Ending.DID_NOT_PASS;
                                game.changeScreen(EScreen.BAD_END);
                            }
                        }
                        default -> {
                            Gdx.app.log(TAG, "Detect item of unexpected type");
                        }
                    }
                }
                case "ITEM", "DIALOG", "TEST"-> {
                }
                case "EXIT" -> {

                }
                default -> {
                    Gdx.app.log(TAG, "Detect collision of unexpected type");
                }
            }
        } else {
            hasTriggeredDialog = false;
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
                    Gdx.app.log(TAG, "Collide with something...");
                    if(hasTriggeredDialog) break;
                    hasTriggeredDialog = true;
                    // What do player collide
                    String type = tile.getProperties().get("type", String.class);
                    switch (type) {
                        // Collide with pick-up-able item
                        case "TEST" -> {
                            // 只能觸發一次
                            String testType = tile.getProperties().get("test", String.class);
                            switch (testType) {
                                case "RETURN_BOOK" -> {
                                    // Interact with librarian
                                    if (!gameState.hasItem(ItemComponent.ItemType.BOOK)) {
                                        String text = "你依稀記得宿舍中有本書還沒還，書沒還可是必不了業的！";
                                        notifyDialogListeners(text);
                                    } else if (!gameState.hasItem(ItemComponent.ItemType.WALLET)) {
                                        game.ending = Ending.AWKWARD_STORE;
                                        game.changeScreen(EScreen.BAD_END);
                                    } else {
                                        String text = "你將書交還給並繳交了逾期費，離畢業又更近了一步 (要有書和錢包)";
                                        notifyDialogListeners(text);
                                    }

                                }
                            }
                        }
                        // Collide with dialogable area
                        case "DIALOG" -> {
                            Gdx.app.log(TAG, "Collide with DIALOG");
                            // 不會失敗，可能需要處理邏輯
                            String dialogType = tile.getProperties().get("dialog", String.class);
                            String text = tile.getProperties().get("text", String.class);
                            switch (dialogType) {
                                case "APPLE", "BOOK", "ENGLISH", "WALLET" -> {
                                    // Pickup the apple under the tree in school gate
                                    // Pickup the book in dorm room
                                    // Get english transcript in dorm room
                                    // Get wallet in dorm room
                                    if (!gameState.hasItem(ItemComponent.ItemType.valueOf(dialogType))) {
                                        gameState.addItem(ItemComponent.ItemType.valueOf(dialogType));
                                        Gdx.app.log(TAG, "Item \"" + dialogType + "\" added to gameState");
                                        notifyDialogListeners(text);
                                    } else {
                                        Gdx.app.log(TAG, "Item \"" + dialogType + "\" already exist.");
                                        Gdx.app.log(TAG, gameState.items.toString());
                                    }
                                }
                                case "COMPUTER" -> {
                                    // 線上畢業審核
                                    // TODO: Do examine and generate text
                                }
                                case "EXHIBIT" -> {
                                    // 拿時數
                                    if (!gameState.hasEnoughHours) {
                                        // Only evoke dialog if not triggered before
                                        // TODO: Trigger dialog

                                        gameState.hasEnoughHours = true;
                                    }
                                }
                                default -> {
                                    // 不須處理邏輯的對話，可以在Tiled中直接設定
                                    notifyDialogListeners(text);
                                }
                            }
                        }
                        case "MAP", "EVENT", "EXIT" -> {
                        }
                        default -> Gdx.app.log(TAG, "Detect dialog of unexpected type");
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

    private void notifyDialogListeners(String text) {
        for (PlayerInteractionListener listener : playerInteractionListeners) {
            listener.onDialog(text);
        }
    }

    public interface PlayerInteractionListener {
        void onDialog(String text);
    }
}
