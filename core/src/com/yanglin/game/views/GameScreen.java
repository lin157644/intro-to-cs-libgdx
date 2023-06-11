package com.yanglin.game.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.component.*;
import com.yanglin.game.entity.systems.AnimationSystem;
import com.yanglin.game.entity.systems.PlayerSystem;
import com.yanglin.game.entity.systems.RenderingSystem;
import com.yanglin.game.input.GameInputProcessor;

public class GameScreen implements Screen {
    private final EntityEngine engine;
    private final GameAssetManager assetManager;

    private final IWantToGraduate game;

    private final OrthographicCamera camera;

    public GameScreen(IWantToGraduate game) {
        this.game = game;

        this.engine = new EntityEngine();

        this.assetManager = game.assetManager;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        // Camera show a specific size of area in game world, which is independent to the window size.
        camera.setToOrtho(false, (w / h) * 20, 20);
    }

    @Override
    public void show() {
        // Create systems
        final RenderingSystem renderingSystem = new RenderingSystem(assetManager, camera, new SpriteBatch());
        final PlayerSystem playerSystem = new PlayerSystem(camera, assetManager);
        final AnimationSystem playerAnimationSystem = new AnimationSystem(assetManager);

        // Add systems
        engine.addSystem(renderingSystem);
        engine.addSystem(playerSystem);
        engine.addSystem(playerAnimationSystem);

        // Create item entity
        for (ItemComponent.ItemType t : ItemComponent.ItemType.values()) {
            Entity entity = engine.createEntity();
            ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
            itemComponent.type = t;
            entity.add(itemComponent);
            engine.addEntity(entity);
        }

        // Create player entities
        Entity playerEntity = engine.createEntity();
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        // TODO: Initialize component
        positionComponent.position.x = 10;
        positionComponent.position.y = 10;
        ArrayMap<String, Animation<TextureRegion>> tmp = new ArrayMap<>();
        for (GameAssetManager.PlayerAnimation pa : GameAssetManager.PlayerAnimation.values()) {
            animationComponent.addAnimation(pa.getType(), new Animation<>(0.1f, assetManager.playerAnimationFrames.get(pa)));
        }
        stateComponent.set("IDLE_FRONT");
        stateComponent.setLooping(true);

        playerEntity.add(playerComponent);
        playerEntity.add(positionComponent);
        playerEntity.add(animationComponent);
        playerEntity.add(textureComponent);
        playerEntity.add(stateComponent);
        engine.addEntity(playerEntity);

        // Create HID entities
        Entity fpsEntity = engine.createEntity();
        FontComponent fontComponent = engine.createComponent(FontComponent.class);
        fontComponent.font = new BitmapFont(); // use libGDX's default Arial font
        fpsEntity.add(fontComponent);
        engine.addEntity(fpsEntity);

        // engine.addSystem(new RenderSystem());
        InputMultiplexer multiplexer = new InputMultiplexer();
        GameInputProcessor gameInputProcessor = new GameInputProcessor();
        multiplexer.addProcessor(gameInputProcessor);
        gameInputProcessor.addKeyInputProcessor(playerSystem);
        // multiplexer.addProcessor(new PauseInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 0.0f);
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
    }
}
