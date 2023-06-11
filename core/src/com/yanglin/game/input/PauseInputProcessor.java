package com.yanglin.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class PauseInputProcessor implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        // Return true if event is handled
        Boolean isHandled = false;
        // TODO: Check if is in pause menu state
        if(false) {
            switch (keycode) {
                case Input.Keys.UP -> {
                    // Move
                    isHandled = true;
                }
                case Input.Keys.DOWN -> {
                    isHandled = true;
                }
                case Input.Keys.Z -> {
                    isHandled = true;
                }
            }
        }
        return isHandled;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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
