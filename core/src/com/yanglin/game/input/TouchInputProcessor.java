package com.yanglin.game.input;

import com.badlogic.gdx.Input;

public interface TouchInputProcessor {
    boolean touchDown(int screenX, int screenY, int pointer, int button);


    boolean touchUp(int screenX, int screenY, int pointer, int button);


    boolean touchDragged(int screenX, int screenY, int pointer);
}
