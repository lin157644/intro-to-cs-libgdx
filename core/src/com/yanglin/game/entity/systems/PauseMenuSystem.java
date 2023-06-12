package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import com.yanglin.game.entity.component.ButtonTextureComponent;
import com.yanglin.game.entity.component.PositionComponent;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.input.MouseInputProcessor;

public class PauseMenuSystem extends EntitySystem implements KeyInputListener, MouseInputProcessor {
    // Renderable entities in pausemenusystem should have a z level higher than 1;
    private Family buttonFamily;
    private Family toggleFamily;

    private Boolean isPaused;

    public PauseMenuSystem(Boolean isPaused) {
        this.isPaused = isPaused;
        buttonFamily = Family.all(ButtonTextureComponent.class, PositionComponent.class).get();
        toggleFamily = Family.all(ButtonTextureComponent.class, PositionComponent.class).get();
    }

    @Override
    public void update(float deltaTime){
        // Update
        if (isPaused){
            // Change the menu to render

        }
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        // Return true if event is handled
        // TODO: Check if is in pause menu state
        if(isPaused) {
            switch (keycode) {
                case Input.Keys.UP -> {
                    // Move
                }
                case Input.Keys.DOWN -> {
                    //
                }
                case Input.Keys.Z -> {
                    //
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        if(isPaused) {
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
