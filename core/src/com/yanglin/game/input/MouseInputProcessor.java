package com.yanglin.game.input;

public interface MouseInputProcessor {
    boolean mouseMoved (int screenX, int screenY);
    boolean scrolled (float amountX, float amountY);
}
