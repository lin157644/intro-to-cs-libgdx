package com.yanglin.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class IWantToGraduate extends Game {
    // ApplicationAdapter is more basic than Game
    // Allow fancier transition
    
    public enum Screens {
        MENU,
        PLAY,
        PAUSE,
        BAD_END,
        GOOD_END,
    }

    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        Engine engine = new Engine();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 0.0f);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
