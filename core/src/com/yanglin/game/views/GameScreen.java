package com.yanglin.game.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.MusicManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.entity.component.*;
import com.yanglin.game.entity.systems.*;
import com.yanglin.game.input.CursorProcessor;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.MouseInputProcessor;

public class GameScreen implements Screen {
    private static final String TAG = GameScreen.class.getSimpleName();
    public static final int viewPortHeight = 15;
    private EntityEngine engine;
    private GameAssetManager assetManager;
    private MapManager mapManager;
    private final IWantToGraduate game;
    private final OrthographicCamera camera;
    public Stage uistage;
    public Stage dialogstage;
    private SpriteBatch batch;
    private Boolean playMusic = true;
    private Boolean playEffect = true;
    public Boolean isPaused = false;

    public GameScreen(IWantToGraduate game) {
        Gdx.app.log(TAG, "GameScreen constructor");
        this.game = game;

        batch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        // Camera show a specific size of area in game world, which is independent to the window size.
        camera.setToOrtho(false, (w / h) * viewPortHeight, viewPortHeight);

        uistage = new Stage();
        dialogstage = new Stage();
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
        if (playMusic) {
            game.musicManager.playBGM();
        } else {
            game.musicManager.pauseBGM();
        }
    }

    public Boolean getPlayMusic() {
        return playMusic;
    }

    public void setPlayEffect(Boolean playEffect) {
        this.playEffect = playEffect;
    }

    public Boolean getPlayEffect() {
        return playEffect;
    }

    public void setPaused(Boolean isPaused) {
        this.isPaused = isPaused;
    }

    @Override
    public void show() {
        Gdx.app.debug(TAG, "Showed");

        if (game.gameState.month < 6)
            game.musicManager.setBGM(MusicManager.BGM.GAME, true);
        else
            game.musicManager.setBGM(MusicManager.BGM.GAME_JUNE, true);

        this.engine = new EntityEngine();
        this.assetManager = game.assetManager;
        this.mapManager = game.mapManager;

        mapManager.clearMapListeners();
        mapManager.setCurrentMap(game.gameState.map, 24, 20);

        // Create systems
        final RenderingSystem renderingSystem = new RenderingSystem(assetManager, mapManager, camera, batch);
        final PlayerMovementSystem playerMovementSystem = new PlayerMovementSystem(assetManager, mapManager, camera, game.gameState);
        final AnimationSystem playerAnimationSystem = new AnimationSystem(assetManager);
        final HUDSystem HUDSystem = new HUDSystem(game, uistage, this);
        final PlayerInteractionSystem playerInteractionSystem = new PlayerInteractionSystem(game);
        final TimeSystem timeSystem = new TimeSystem(game, this);
        final DialogSystem dialogSystem = new DialogSystem(game, dialogstage, this);
        // Add systems
        engine.addSystem(renderingSystem);
        engine.addSystem(playerMovementSystem);
        engine.addSystem(playerAnimationSystem);
        engine.addSystem(HUDSystem);
        engine.addSystem(playerInteractionSystem);
        engine.addSystem(timeSystem);
        engine.addSystem(dialogSystem);

        timeSystem.addTimeSystemListener(playerMovementSystem);
        timeSystem.addTimeSystemListener(dialogSystem);
        playerInteractionSystem.addPlayerInteractionListener(dialogSystem);

        // Create item entity
        for (ItemComponent.ItemType itemType : ItemComponent.ItemType.values()) {
            Entity entity = engine.createEntity();
            ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
            itemComponent.type = itemType;
            entity.add(itemComponent);
            engine.addEntity(entity);
        }

        // Create player entities
        Entity playerEntity = engine.createEntity();
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        RenderableComponent renderableComponent = engine.createComponent(RenderableComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);

        // TODO: Initialize components
        positionComponent.position.x = game.gameState.x;
        positionComponent.position.y = game.gameState.y;
        positionComponent.map = game.gameState.map;
        ArrayMap<String, Animation<TextureRegion>> tmp = new ArrayMap<>();
        for (GameAssetManager.PlayerAnimation pa : GameAssetManager.PlayerAnimation.values()) {
            animationComponent.addAnimation(pa.getType(), new Animation<>(0.1f, assetManager.playerAnimationFrames.get(pa)));
        }
        stateComponent.set("IDLE_FRONT");
        stateComponent.setLooping(true);

        playerEntity.add(playerComponent).add(positionComponent).add(animationComponent).add(textureComponent).add(renderableComponent).add(stateComponent);
        engine.addEntity(playerEntity);

        Array<Texture> itemTextures = new Array<>();
        itemTextures.add(game.assetManager.get("ui/wallet.png"), game.assetManager.get("ui/apple_basket.png"), game.assetManager.get("ui/english.png"), game.assetManager.get("ui/book.png"));
        Array<Vector2> itemPositions = new Array<Vector2>();

        float pauseTileUnit = 48f;
        float centerX = uistage.getWidth() / 2;
        float centerY = uistage.getHeight() / 2;
        itemPositions.add(new Vector2(centerX + pauseTileUnit * -0.5f, centerY + pauseTileUnit * 0.5f),
                new Vector2(centerX + pauseTileUnit * 1.5f, centerY + pauseTileUnit * 0.5f),
                new Vector2(centerX + pauseTileUnit * 3.5f, centerY + pauseTileUnit * 0.5f),
                new Vector2(centerX + pauseTileUnit * -0.5f, centerY + pauseTileUnit * -1.5f));
        // Create Item entities
        int index = 0;
        for (ItemComponent.ItemType itemType : ItemComponent.ItemType.values()) {
            ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
            RenderableComponent itemRenderableComponent = engine.createComponent(RenderableComponent.class);
            TextureComponent itemTextureComponent = engine.createComponent(TextureComponent.class);
            PositionComponent itemPositionComponent = engine.createComponent(PositionComponent.class);

            itemComponent.type = itemType;
            itemPositionComponent.position.set(itemPositions.get(index));
            itemTextureComponent.region = new TextureRegion(itemTextures.get(index));
            // renderableComponent.isVisible = false;

            Entity itemEntity = engine.createEntity();
            itemEntity.add(itemComponent).add(itemRenderableComponent).add(itemTextureComponent).add(itemPositionComponent);
            engine.addEntity(itemEntity);
            index++;
        }

        // Input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        GameInputProcessor gameInputProcessor = new GameInputProcessor();

        // Priority: Pause -> Dialog -> Interaction(ZX + Force Event) ->  Movement(WASD)
        gameInputProcessor.addKeyInputProcessor(HUDSystem);
        gameInputProcessor.addKeyInputProcessor(dialogSystem);
        gameInputProcessor.addKeyInputProcessor(playerInteractionSystem);
        gameInputProcessor.addKeyInputProcessor(playerMovementSystem);

        InputProcessor cursorProcessor = new CursorProcessor(game);
        inputMultiplexer.addProcessor(cursorProcessor);
        inputMultiplexer.addProcessor(uistage);
        inputMultiplexer.addProcessor(gameInputProcessor);

        Gdx.input.setInputProcessor(inputMultiplexer);

        Entity player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        EntityEngine.positionComponentMapper.get(player).position.x = game.gameState.x;
        EntityEngine.positionComponentMapper.get(player).position.y = game.gameState.y;

        if (!game.gameState.hasPlayedIntroDialog) {
            dialogSystem.onDialog("{SLOWER}11X年的夏天，我在校園裡的路X莎喝著咖啡，大學生活真輕鬆啊{EVENT=stopDialogEffect}\n{WAIT}什{EVENT=playDialogEffect}麼?已經到了要畢業的時間了???{EVENT=stopDialogEffect}\n這{EVENT=playDialogEffect}可不行，得趕緊回宿舍準備需要的東西!", false);
            game.gameState.hasPlayedIntroDialog = true;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.7f, 1.0f);
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        engine.getSystem(RenderingSystem.class).dispose();
        engine.removeAllEntities();

        engine.removeAllSystems();
    }
}
