package com.yanglin.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yanglin.game.GameAssetManager;
import com.yanglin.game.IWantToGraduate;


public class LoadingScreen implements Screen {
    private final GameAssetManager assetManager;
    private final IWantToGraduate game;
    private static final int blockTime = 17;
    private static final SpriteBatch batch = new SpriteBatch();
    private int currentLoadingStage = 1;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private BitmapFont loadingFont = new BitmapFont();

    public LoadingScreen(IWantToGraduate game) {
        this.assetManager = game.assetManager;
        this.game = game;
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    }

    @Override
    public void show() {
        assetManager.loadTiledMap();
        assetManager.loadImages();
        assetManager.loadFonts();
        assetManager.loadSkins();
        assetManager.loadAudios();
    }

    @Override
    public void render(float delta) {
        if (game.assetManager.update(blockTime)) { // Load some, will return true if done loading
            assetManager.buildPlayerAnimationFrames();
            game.changeScreen(EScreen.MENU);
            currentLoadingStage += 1;
            // 32
        }
        batch.begin();
        loadingFont.draw(batch, "Loading", Gdx.graphics.getWidth() / 2f - 30, Gdx.graphics.getHeight() / 2f + 50);
        batch.end();
        float progress = assetManager.getProgress();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(Gdx.graphics.getWidth() * (1 - progress) / 2, Gdx.graphics.getHeight() / 2f - 25, Gdx.graphics.getWidth() * progress, 50);
        shapeRenderer.end();


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
