package com.yanglin.game.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.component.*;
import com.yanglin.game.entity.systems.*;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.ui.PauseButton;
import com.yanglin.game.ui.PauseToggle;

public class GameScreen implements Screen {
    private final EntityEngine engine;
    private final GameAssetManager assetManager;
    private final IWantToGraduate game;
    private final OrthographicCamera camera;
    public Stage uistage;
    private SpriteBatch batch;
    private Boolean playMusic = true;
    private Boolean playEffect = true;


    public Boolean isPaused = false;

    public GameScreen(IWantToGraduate game) {
        this.game = game;

        this.engine = new EntityEngine();

        this.assetManager = game.assetManager;

        batch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        // Camera show a specific size of area in game world, which is independent to the window size.
        camera.setToOrtho(false, (w / h) * 20, 20);

        // UI
        uistage = new Stage();
        // new FitViewport(1280, 720, new OrthographicCamera()), batch
    }

    @Override
    public void show() {
        // Create systems
        final RenderingSystem renderingSystem = new RenderingSystem(assetManager, camera, batch, game.mapManager);
        final PlayerMovementSystem playerMovementSystem = new PlayerMovementSystem(camera, assetManager, game.mapManager);
        final AnimationSystem playerAnimationSystem = new AnimationSystem(assetManager);
        final PauseMenuSystem pauseMenuSystem = new PauseMenuSystem(assetManager, uistage, isPaused);
        final PlayerInteractionSystem playerInteractionSystem = new PlayerInteractionSystem(assetManager, game.mapManager);
        // Add systems
        engine.addSystem(renderingSystem);
        engine.addSystem(playerMovementSystem);
        engine.addSystem(playerAnimationSystem);
        engine.addSystem(pauseMenuSystem);
        engine.addSystem(playerInteractionSystem);

        game.mapManager.addMapListener(playerInteractionSystem);
        game.mapManager.addMapListener(renderingSystem);

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

        // Create HID entities
        Entity fpsEntity = engine.createEntity();
        FontComponent fontComponent = engine.createComponent(FontComponent.class);
        RenderableComponent fpsrenderableComponent = engine.createComponent(RenderableComponent.class);
        fontComponent.font = new BitmapFont(); // use libGDX's default Arial font
        fpsEntity.add(fontComponent).add(fpsrenderableComponent);
        engine.addEntity(fpsEntity);

        // Create Item entities
        for (ItemComponent.ItemType itemType : ItemComponent.ItemType.values()) {
            ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
            RenderableComponent itemRenderableComponent = engine.createComponent(RenderableComponent.class);
            TextureComponent itemTextureComponent = engine.createComponent(TextureComponent.class);
            PositionComponent itemPositionComponent = engine.createComponent(PositionComponent.class);
            // TODO: Initialize items
            Entity itemEntity = engine.createEntity();
            itemEntity.add(itemComponent).add(itemRenderableComponent).add(itemTextureComponent).add(itemPositionComponent);
            engine.addEntity(itemEntity);
        }

        // Create pause menu


        // Input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        GameInputProcessor gameInputProcessor = new GameInputProcessor();

        gameInputProcessor.addKeyInputProcessor(pauseMenuSystem);
        gameInputProcessor.addKeyInputProcessor(playerMovementSystem);

        inputMultiplexer.addProcessor(uistage);
        inputMultiplexer.addProcessor(gameInputProcessor);

        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 0.0f);
        engine.update(delta);
        // uistage.act(delta);
        // uistage.draw();
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
        engine.removeAllEntities();
        engine.removeAllSystems();
    }
}
