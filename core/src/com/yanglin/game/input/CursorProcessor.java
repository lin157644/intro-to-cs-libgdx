package com.yanglin.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.yanglin.game.IWantToGraduate;

public class CursorProcessor extends InputAdapter {
    IWantToGraduate game;
    public CursorProcessor(IWantToGraduate game){
        this.game = game;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.graphics.setCursor(game.cursor_pressed);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.graphics.setCursor(game.cursor);
        return false;
    }
}
