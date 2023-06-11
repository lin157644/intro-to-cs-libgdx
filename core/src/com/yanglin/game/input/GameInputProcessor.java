package com.yanglin.game.input;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

public class GameInputProcessor implements InputProcessor {
    // We may implement InputProcessor or InputAdaptor
    private final ArrayList<KeyInputListener> keyInputListeners;
    private final ArrayList<MouseInputProcessor> mouseInputProcessors;
    private final boolean[] keyState;

    public GameInputProcessor() {
        this.keyInputListeners = new ArrayList<>();
        this.mouseInputProcessors = new ArrayList<>();
        this.keyState = new boolean[256];
    }

    public void addKeyInputProcessor(final KeyInputListener processor) {
        keyInputListeners.add(processor);
    }

    public void removeKeyInputProcessor(final KeyInputListener processor) {
        keyInputListeners.remove(processor);
    }

    public void addMouseInputProcessor(final MouseInputProcessor processor) {
        mouseInputProcessors.add(processor);
    }

    public void removeMouseInputProcessor(final MouseInputProcessor processor) {
        mouseInputProcessors.remove(processor);
    }

    public boolean isKeyDown(int keycode){
        return keyState[keycode];
    }

    @Override
    public boolean keyDown(int keycode) {
        keyState[keycode] = true;
        for (KeyInputListener processor : keyInputListeners) {
            processor.keyDown(this, keycode);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyState[keycode] = false;
        for (KeyInputListener processor : keyInputListeners) {
            processor.keyUp(this, keycode);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        for (KeyInputListener processor : keyInputListeners) {
            processor.keyTyped(this, character);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // last.set(-1, -1, -1);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // camera.unproject(curr.set(screenX, screenY, 0));
        // if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
        //     camera.unproject(delta.set(last.x, last.y, 0));
        //     delta.sub(curr);
        //     camera.position.add(delta.x, delta.y, 0);
        // }
        // last.set(screenX, screenY, 0);
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
