package com.yanglin.game.views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.MainAssetManager;

public class LoadingScreen implements Screen {
    private final MainAssetManager assetManager;
    private final IWantToGraduate game;
    private static final int blockTime = 17;

    public LoadingScreen(IWantToGraduate game){
        this.assetManager = game.assetManager;
        this.game = game;
    }

    @Override
    public void show() {
        assetManager.loadTiledMap();
        assetManager.loadSprites();
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
