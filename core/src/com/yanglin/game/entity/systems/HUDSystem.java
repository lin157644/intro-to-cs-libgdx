package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.GameScreen;

import java.awt.*;

public class HUDSystem extends EntitySystem implements KeyInputListener {
    // Renderable entities in pause menu system should have a z level higher than 1;
    private static String TAG = HUDSystem.class.getSimpleName();
    public Stage stage;
    private IWantToGraduate game;
    private OrthographicCamera hudCamera;
    private BitmapFont debugFont;
    private SpriteBatch debugBatch;
    private GameScreen gameScreen;

    public HUDSystem(IWantToGraduate game, Stage stage, GameScreen gameScreen) {
        // Display pause menu, time, quest
        // Debug: display fps, hunger
        this.stage = stage;
        this.game = game;
        this.gameScreen = gameScreen;

        Gdx.app.debug(TAG, "HUDStage");
        debugBatch = new SpriteBatch();
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
                game.changeScreen(EScreen.MENU);
            }
        });

        pauseResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setPaused(false);
            }
        });

        stage.addActor(pauseBG);
        stage.addActor(pauseMenu);
        stage.addActor(pauseResume);
        stage.addActor(musicToggle);
        stage.addActor(effectToggle);
        stage.addActor(resumeLabel);
        stage.addActor(menuLabel);
    }

    @Override
    public void update(float deltaTime) {
        // Update
        if (gameScreen.isPaused) {
            // Change the menu to render
            stage.act(deltaTime);
            stage.draw();
        }
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugBatch.begin();
            debugFont.draw(debugBatch, "FPS=" + Gdx.graphics.getFramesPerSecond() + " Hunger: " + game.gameState.hunger, 0, hudCamera.viewportHeight);
            debugBatch.end();
        }
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
                }
            }
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            gameScreen.isPaused = true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }
}
