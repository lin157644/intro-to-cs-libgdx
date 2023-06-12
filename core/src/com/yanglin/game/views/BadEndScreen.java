package com.yanglin.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.yanglin.game.IWantToGraduate;

public class BadEndScreen implements Screen {
    private static String TAG = BadEndScreen.class.getSimpleName();
    private IWantToGraduate game;
    public BadEndScreen(IWantToGraduate game) {
        this.game = game;
    }
    @Override
    public void show() {
        switch (game.ending) {
            case DEFAULT_BAD -> {
                // Exit the school before finish all quest
            }
            case HUNGER -> {

            }
            case OVERSLEEP -> {

            }
            case CAR_CRUSH -> {

            }
            case DID_NOT_PASS -> {

            }
            case LOW_ENG_SCORE -> {

            }
            default -> Gdx.app.log(TAG, "Unimplemented ending or null");
        }

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
