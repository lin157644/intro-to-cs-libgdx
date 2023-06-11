package com.yanglin.game.input;

public interface KeyInputListener {
    boolean keyDown(GameInputProcessor gameInputProcessor, int keycode);

    boolean keyUp(GameInputProcessor gameInputProcessor, int keycode);

    boolean keyTyped(GameInputProcessor gameInputProcessor, char character);
}
