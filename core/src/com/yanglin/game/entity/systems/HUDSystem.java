package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;

public class HUDSystem extends EntitySystem implements KeyInputListener {
    // Renderable entities in pause menu system should have a z level higher than 1;
    private Boolean isPaused;
    public Stage stage;
    private IWantToGraduate game;

    private OrthographicCamera hudCamera;

    public HUDSystem(IWantToGraduate game, Stage stage, Boolean isPaused) {
        // Deal with pause menu, time display
        this.stage = stage;
        this.isPaused = isPaused;
        this.game = game;

        // hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // hudCamera.position.set(hudCamera.viewportWidth / 2.0f, hudCamera.viewportHeight / 2.0f, 1.0f);

        Texture t = game.assetManager.get("tilesets/Modern_UI_Style_2_48x48.png");

        int uiTileSize = 48;
        TextureRegion[][] toggleRegion = (new TextureRegion(t, 19 * uiTileSize, 26 * uiTileSize, uiTileSize * 4, uiTileSize)).split(uiTileSize * 2, uiTileSize);
        TextureRegion[][] buttonRegion = (new TextureRegion(t, 24 * uiTileSize, 25 * uiTileSize, uiTileSize * 19, uiTileSize * 9)).split(uiTileSize, uiTileSize);

        TextureRegionDrawable toggleOffDrawable = new TextureRegionDrawable(toggleRegion[0][0]);
        TextureRegionDrawable toggleOnDrawable = new TextureRegionDrawable(toggleRegion[0][1]);

        TextureRegionDrawable menuNormalDrawable = new TextureRegionDrawable(buttonRegion[5][8]);
        TextureRegionDrawable menuHoverDrawable = new TextureRegionDrawable(buttonRegion[5][9]);
        TextureRegionDrawable menuPressedDrawable = new TextureRegionDrawable(buttonRegion[5][10]);

        TextureRegionDrawable resumeNormalDrawable = new TextureRegionDrawable(buttonRegion[7][0]);
        TextureRegionDrawable resumeHoverDrawable = new TextureRegionDrawable(buttonRegion[7][1]);
        TextureRegionDrawable resumePressedDrawable = new TextureRegionDrawable(buttonRegion[7][2]);

        Container<Button> playMusicToggleContainer = new Container<>(new Button(toggleOnDrawable, toggleOnDrawable, toggleOffDrawable));
        Container<Button> playEffectToggleContainer = new Container<>(new Button(toggleOnDrawable, toggleOnDrawable, toggleOffDrawable));
        playMusicToggleContainer.setPosition(400, 400);
        playEffectToggleContainer.setPosition(400, 500);
        // playMusicToggleContainer.getActor().addAction();

        Container<Button> resumeButtonContainer = new Container<>(new Button(menuNormalDrawable, menuPressedDrawable, menuNormalDrawable));
        Container<Button> menuButtonContainer = new Container<>(new Button(menuNormalDrawable, menuPressedDrawable, menuNormalDrawable));
        resumeButtonContainer.setPosition(400, 300);
        menuButtonContainer.setPosition(400, 350);

        stage.addActor(playMusicToggleContainer);
        stage.addActor(playEffectToggleContainer);
        stage.addActor(resumeButtonContainer);
        stage.addActor(menuButtonContainer);
    }

    @Override
    public void update(float deltaTime) {
        // Update
        if (isPaused) {
            // Change the menu to render
            stage.act(deltaTime);
            stage.draw();
        }
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        // Return true if event is handled
        if (isPaused) {
            switch (keycode) {
                case Input.Keys.UP -> {
                    // Move
                }
                case Input.Keys.DOWN -> {
                    //
                }
                case Input.Keys.Z -> {
                    game.changeScreen(EScreen.MENU);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        if (isPaused) {
            switch (keycode) {
                case Input.Keys.ESCAPE -> {
                    isPaused = false;
                }
            }
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            isPaused = true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }
}
