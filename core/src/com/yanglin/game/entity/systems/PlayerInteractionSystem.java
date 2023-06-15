package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.MusicManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.ItemComponent;
import com.yanglin.game.entity.component.PlayerComponent;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.Ending;

public class PlayerInteractionSystem extends EntitySystem implements MapManager.MapListener, KeyInputListener {
    // Player <-> Item
    // Use tiled object layer
    private static final String TAG = PlayerInteractionSystem.class.getSimpleName();
    private TiledMapTileLayer objectLayer;
    private MapManager mapManager;
    private GameAssetManager assetManager;
    private GameState gameState;
    private IWantToGraduate game;
    private final Array<PlayerInteractionListener> playerInteractionListeners = new Array<>();
    private Family family;
    private ImmutableArray<Entity> entities;

    private boolean hasTriggeredDialog = false;

    public PlayerInteractionSystem(IWantToGraduate game) {
        super(0);
        this.family = Family.all(PlayerComponent.class, PositionComponent.class).get();

        this.game = game;
        this.assetManager = game.assetManager;
        this.mapManager = game.mapManager;
        this.gameState = game.gameState;
        mapManager.addMapListener(this);

        TiledMap tiledMap = assetManager.get(mapManager.getCurrentMap().getFileName());
        objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("obj");
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        entities = null;
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
    public void update(float deltaTime) {
        // Change screen 後需加上return?
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
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
                        if (hasTriggeredDialog) break;
                        String eventType = tile.getProperties().get("event", String.class);
                        switch (eventType) {
                            case "SHOP" -> {
                                hasTriggeredDialog = true;
                                if (!gameState.hasEaten) {
                                    if (!gameState.hasItem(ItemComponent.ItemType.WALLET)) {
                                        game.ending = Ending.AWKWARD_STORE;
                                        game.changeScreen(EScreen.BAD_END);
                                        return;
                                    } else {
                                        gameState.hasEaten = true;
                                        String text = "你到超商買了些東西，填飽了肚子才有力氣忙畢業嘛。";
                                        notifyDialogListeners(text, true);
                                    }
                                }
                            }
                            case "BUS" -> {
                                hasTriggeredDialog = true;
                                // TODO: Play animation
                                game.ending = Ending.CAR_CRUSH;
                                game.changeScreen(EScreen.BAD_END);
                                return;
                            }
                            case "GIVE_ENGLISH" -> {
                                hasTriggeredDialog = true;
                                if (!gameState.hasItem(ItemComponent.ItemType.ENGLISH)) {
                                    String text = "你連成績單都沒帶就想要畢業，回去宿舍找找吧！";
                                    notifyDialogListeners(text, true);

                                } else if (!gameState.hasWorshiped) {
                                    // Fail text won't be use here
                                    game.ending = Ending.LOW_ENG_SCORE;
                                    game.changeScreen(EScreen.BAD_END);
                                    return;
                                } else {
                                    String text = "你顫抖得將多益成績單交給了語言中心服務人員 {NEXT} 在填寫了資料後，申請通過畢業門檻。";
                                    game.gameState.hasPassEnglish = true;
                                    notifyDialogListeners(text, true);
                                }

                            }
                            case "GIFT_PROF" -> {
                                hasTriggeredDialog = true;
                                if (!gameState.hasMeetProf) {
                                    if (gameState.hasItem(ItemComponent.ItemType.APPLE)) {
                                        String text = "教授覺得你上課認真，決定同情你讓你畢業。";
                                        notifyDialogListeners(text, true);
                                        gameState.hasMeetProf = true;
                                    } else {
                                        // Fail text won't be use here
                                        game.ending = Ending.DID_NOT_PASS;
                                        game.changeScreen(EScreen.BAD_END);
                                        return;
                                    }
                                }
                            }
                            case "ACAD_PROC" -> {
                                hasTriggeredDialog = true;
                                if (game.gameState.hasWorshiped && game.gameState.hasPassEnglish && game.gameState.hasEnoughHours) {
                                    game.gameState.hasFinishedProcedure = true;
                                    String text = "你已經完成離校手續領到了學位證書，可以正式告別中央了。";
                                    notifyDialogListeners(text, true);

                                } else {
                                    String text = "看來你距離畢業還有些事情沒做。{NEXT}";
                                    notifyDialogListeners(text + getHintDialog(), true);
                                }
                            }
                            default -> {
                                Gdx.app.log(TAG, "Detect item of unexpected type");
                            }
                        }
                    }
                    case "ITEM", "DIALOG", "TEST", "DEBUG" -> {
                    }
                    case "EXIT" -> {
                        if (game.gameState.canGraduate()) {
                            game.ending = Ending.GOOD_END;
                            game.changeScreen(EScreen.GOOD_END);
                            return;
                        } else {
                            game.ending = Ending.DEFAULT_BAD;
                            game.changeScreen(EScreen.BAD_END);
                            return;
                        }
                    }
                    default -> {
                        Gdx.app.log(TAG, "Detect collision of unexpected type");
                    }
                }
            } else {
                hasTriggeredDialog = false;
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
                Entity entity = getEngine().getEntitiesFor(family).first();
                PositionComponent positionComponent = EntityEngine.positionComponentMapper.get(entity);
                // Do player collide with object?
                TiledMapTile tile;
                if ((tile = collideWithObject(positionComponent.position.x, positionComponent.position.y)) != null) {
                    Gdx.app.log(TAG, "Collide with something...");
                    if (hasTriggeredDialog) break;
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
                                        notifyDialogListeners(text, false);
                                    } else if (!gameState.hasItem(ItemComponent.ItemType.WALLET)) {
                                        game.ending = Ending.AWKWARD_STORE;
                                        game.changeScreen(EScreen.BAD_END);
                                    } else {
                                        String text = "你將書交還給並繳交了逾期費，離畢業又更近了一步。";
                                        notifyDialogListeners(text, false);
                                        game.gameState.hasReturnBook = true;
                                    }

                                }
                            }
                        }
                        // Collide with dialogable area
                        case "DIALOG" -> {
                            Gdx.app.log(TAG, "Collide with DIALOG");
                            // 不會失敗，可能需要處理邏輯
                            String dialogType = tile.getProperties().get("dialog", String.class);
                            String dialogText = tile.getProperties().get("text", String.class);
                            switch (dialogType) {
                                case "APPLE", "BOOK", "ENGLISH", "WALLET" -> {
                                    // Pickup the apple under the tree in school gate
                                    // Pickup the book in dorm room
                                    // Get english transcript in dorm room
                                    // Get wallet in dorm room
                                    if (!gameState.hasItem(ItemComponent.ItemType.valueOf(dialogType))) {
                                        gameState.addItem(ItemComponent.ItemType.valueOf(dialogType));
                                        Gdx.app.log(TAG, "Item \"" + dialogType + "\" added to gameState");
                                        notifyDialogListeners(dialogText, false);
                                        game.musicManager.playEffect(MusicManager.Effect.GET_ITEM);
                                    } else {
                                        Gdx.app.log(TAG, "Item \"" + dialogType + "\" already exist.");
                                        Gdx.app.log(TAG, gameState.items.toString());
                                    }
                                }
                                case "EXHIBIT" -> {
                                    // 拿時數
                                    if (!gameState.hasEnoughHours) {
                                        // Only evoke dialog if not triggered before
                                        gameState.hasEnoughHours = true;
                                        notifyDialogListeners(dialogText, true);
                                    }
                                }
                                case "WORSHIP" -> {
                                    if (!gameState.hasWorshiped) {
                                        gameState.hasWorshiped = true;
                                        notifyDialogListeners(dialogText, true);
                                    }
                                }
                                default -> {
                                    // 不須處理邏輯的對話，可以在Tiled中直接設定
                                    notifyDialogListeners(dialogText, false);
                                }
                            }
                        }
                        case "MAP", "EVENT", "EXIT" -> {
                        }
                        case "DEBUG" -> {
                            Gdx.app.log(TAG, "DEBUG Tile: To good end");
                            String debugType = tile.getProperties().get("debug", String.class);
                            switch (debugType) {
                                case "GOOD_END" -> {
                                    game.ending = Ending.GOOD_END;
                                    game.changeScreen(EScreen.GOOD_END);
                                }
                            }
                        }
                        default -> Gdx.app.log(TAG, "Detect dialog of unexpected type");
                    }
                }
            }
        }
        return false;
    }

    public String getHintDialog() {
        // 看來你距離畢業還有些事情沒做。
        if (!game.gameState.hasPassEnglish) {
            if (!game.gameState.hasItem(ItemComponent.ItemType.ENGLISH))
                return "宿舍有張多益成績單，應該可以用來通過畢業門檻。";
            if (!game.gameState.hasWorshiped)
                return "多益的分數好像低空飛過畢業門檻，最好拜拜一下保佑平安畢業。";
            return "把成績單帶去語言中心申請畢業門檻。";
        }
        if (!game.gameState.hasMeetProf) {
            if (!game.gameState.hasItem(ItemComponent.ItemType.APPLE))
                return "還有一科必修沒過，看來需要與教授好好求情，{BR}不知道有沒有適合送給教授的見面禮。";
            return "教授的實驗室在工程五館，最好趕緊過去。";
        }
        if (!game.gameState.hasEnoughHours) {
            return "聽說圖書館正在舉辦書展，服務學習時數就差一點，{BR}應該可以好好利用。";
        }
        if (!game.gameState.hasReturnBook) {
            if (!game.gameState.hasItem(ItemComponent.ItemType.BOOK))
                return "宿舍還有逾期還沒歸還的書，再不還荷包就要大失血了";
            if (!game.gameState.hasItem(ItemComponent.ItemType.WALLET))
                return "書逾期好久了，記得到帶著錢包到圖書館。";
            return "還需要前往圖書館還書才能申請離校手續。";
        }
        if (!game.gameState.hasFinishedProcedure) {
            return "看起來事情都完成得差不多了，註冊組辦理手續吧。";
        }
        return "";
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }

    public void addPlayerInteractionListener(PlayerInteractionListener listener) {
        playerInteractionListeners.add(listener);
    }

    private void notifyDialogListeners(String text, boolean withTransition) {
        for (PlayerInteractionListener listener : playerInteractionListeners) {
            listener.onDialog(text, withTransition);
        }
    }

    public interface PlayerInteractionListener {
        void onDialog(String text, boolean withTransition);
    }
}
