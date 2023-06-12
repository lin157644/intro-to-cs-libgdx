package com.yanglin.game.views;

import com.badlogic.gdx.Screen;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.GameAssetManager;

public class LoadingScreen implements Screen {
    private final GameAssetManager assetManager;
    private final IWantToGraduate game;
    private static final int blockTime = 17;

    public LoadingScreen(IWantToGraduate game){
        this.assetManager = game.assetManager;
        this.game = game;
    }

    @Override
    public void show() {
        assetManager.loadTiledMap();
        assetManager.loadImages();
    }

    @Override
    public void render(float delta) {
        if (assetManager.update(blockTime)) {
            assetManager.buildPlayerAnimationFrames();
            game.changeScreen(EScreen.GAME);
        }

        float progress = assetManager.getProgress();
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
