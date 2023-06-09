package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.entity.component.ItemComponent;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.entity.component.TextureComponent;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.GameScreen;

public class HUDSystem extends EntitySystem implements KeyInputListener {
    // Renderable entities in pause menu system should have a z level higher than 1;
    private static String TAG = HUDSystem.class.getSimpleName();
    public Stage stage;
    private IWantToGraduate game;
    private OrthographicCamera hudCamera;
    private BitmapFont debugFont;
    private SpriteBatch batch;
    private GameScreen gameScreen;
    private Group pauseMenuGroup = new Group();
    private Group timeDisplayGroup = new Group();
    private Family pauseItemFamily;

    public HUDSystem(IWantToGraduate game, Stage stage, GameScreen gameScreen) {
        // Display pause menu, time, quest
        // Debug: display fps, hunger
        this.stage = stage;
        this.game = game;
        this.gameScreen = gameScreen;

        pauseItemFamily = Family.all(ItemComponent.class, PositionComponent.class).get();

        Gdx.app.debug(TAG, "HUDStage");
        batch = new SpriteBatch();
        debugFont = new BitmapFont();
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.position.set(hudCamera.viewportWidth / 2.0f, hudCamera.viewportHeight / 2.0f, 1.0f);

        Texture uiTexture = game.assetManager.get("tilesets/Modern_UI_Style_2_48x48.png");
        Texture pauseBGTexture = game.assetManager.get("ui/ui_pause_bg.png");

        int uiTileSize = 48;
        float centerX = stage.getWidth() / 2, centerY = stage.getHeight() / 2;

        Image pauseBG = new Image(pauseBGTexture);
        pauseBG.setPosition(centerX - pauseBG.getWidth() / 2, centerY - pauseBG.getHeight() / 2);

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");
        Button pauseMenu = new Button(skin, "pauseMenu");
        Button pauseResume = new Button(skin, "pauseResume");

        Button musicToggle = new Button(skin, "pauseToggle");
        Button effectToggle = new Button(skin, "pauseToggle");

        pauseMenu.setPosition(centerX - pauseMenu.getWidth() / 2 - uiTileSize * 5, (centerY - pauseMenu.getHeight() / 2 + uiTileSize));
        pauseResume.setPosition(centerX - pauseResume.getWidth() / 2 - uiTileSize * 5, (centerY - pauseResume.getHeight() / 2 + uiTileSize * 2));
        musicToggle.setPosition((float) (centerX - musicToggle.getWidth() / 2 - uiTileSize * 3.5), (centerY - musicToggle.getHeight() / 2 - uiTileSize));
        effectToggle.setPosition((float) (centerX - effectToggle.getWidth() / 2 - uiTileSize * 3.5), (centerY - effectToggle.getHeight() / 2 - uiTileSize * 2));

        Label resumeLabel = new Label("Resume", skin, "pauseLabel");
        Label menuLabel = new Label("Menu", skin, "pauseLabel");
        resumeLabel.setPosition((float) (centerX - resumeLabel.getWidth() / 2 - uiTileSize * 3.5), (centerY - resumeLabel.getHeight() / 2 + uiTileSize * 2));
        menuLabel.setPosition((float) (centerX - menuLabel.getWidth() / 2 - uiTileSize * 3.85), (centerY - menuLabel.getHeight() / 2 + uiTileSize));

        pauseMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameState.saveState(game.gameState);
                game.changeScreen(EScreen.MENU);
            }
        });

        pauseResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setPaused(false);
                pauseMenuGroup.setVisible(false);
            }
        });

        musicToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.setPlayMusic(!gameScreen.getPlayMusic());
                Gdx.app.log(TAG, "Change play music toggle: " + gameScreen.getPlayMusic());
            }
        });

        effectToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.setPlayEffect(!gameScreen.getPlayEffect());
                Gdx.app.log(TAG, "Change play effect toggle: " + gameScreen.getPlayEffect());
            }
        });

        pauseMenuGroup.addActor(pauseBG);
        pauseMenuGroup.addActor(pauseMenu);
        pauseMenuGroup.addActor(pauseResume);
        pauseMenuGroup.addActor(musicToggle);
        pauseMenuGroup.addActor(effectToggle);
        pauseMenuGroup.addActor(resumeLabel);
        pauseMenuGroup.addActor(menuLabel);
        pauseMenuGroup.setVisible(false);
        stage.addActor(pauseMenuGroup);

        Image timeImage = new Image((Texture) game.assetManager.get("ui/time_display.png"));

        timeImage.setPosition(stage.getWidth() - timeImage.getWidth(), stage.getHeight() - timeImage.getHeight());
        Label timeLabel = new Label("5月1日", skin, "timeLabel");
        timeLabel.setPosition(timeImage.getX() + timeImage.getWidth() / 2 - timeLabel.getWidth() / 2,
                timeImage.getY() + timeImage.getHeight() / 2 - timeLabel.getHeight() / 2 + 22);
        // timeLabel.debug();
        // timeImage.debug();
        timeLabel.setName("");
        timeLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                ((Label) getActor()).setText(game.gameState.month + "月" + game.gameState.date + "日");
                return false;
            }
        });

        timeDisplayGroup.addActor(timeImage);
        timeDisplayGroup.addActor(timeLabel);
        stage.addActor(timeDisplayGroup);

    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();

        batch.begin();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugFont.draw(batch, "FPS=" + Gdx.graphics.getFramesPerSecond() + " Hunger: " + game.gameState.hunger, 0, hudCamera.viewportHeight);
        }
        if (gameScreen.isPaused) {
            ImmutableArray<Entity> entities = getEngine().getEntitiesFor(pauseItemFamily);
            for (Entity itemEntity : entities) {
                ItemComponent itemComponent = EntityEngine.itemComponentMapper.get(itemEntity);
                TextureComponent tex = EntityEngine.textureComponentMapper.get(itemEntity);
                PositionComponent pos = EntityEngine.positionComponentMapper.get(itemEntity);
                if (game.gameState.hasItem(itemComponent.type)) {
                    batch.draw(tex.region, pos.position.x, pos.position.y);
                }
            }
        }
        batch.end();


    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        // Return true if event is handled
        if (gameScreen.isPaused) {
            switch (keycode) {
                case Input.Keys.UP -> {
                    // Move
                }
                case Input.Keys.DOWN -> {
                    //
                }
                case Input.Keys.X -> {
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        if (gameScreen.isPaused) {
            switch (keycode) {
                case Input.Keys.ESCAPE -> {
                    gameScreen.isPaused = false;
                    pauseMenuGroup.setVisible(false);
                }
            }
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            gameScreen.isPaused = true;
            pauseMenuGroup.setVisible(true);
        }
        return false;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }
}
