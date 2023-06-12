package com.yanglin.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.yanglin.game.IWantToGraduate;

public class MenuScreen implements Screen {
    private static final String TAG = MenuScreen.class.getSimpleName();
    private IWantToGraduate game;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont titleFont;
    private Label titleLabel;
    private final Stage stage = new Stage(new ExtendViewport(1280, 720), batch);

    public MenuScreen(IWantToGraduate game) {
        this.game = game;

        FreeTypeFontGenerator generator = game.assetManager.get("fonts/title_font.otf");
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "大延畢";
        parameter.size = 120;

        Skin menuSkin = game.assetManager.get("ui/skins/title_skin.json");
        titleLabel = new Label("TEST", menuSkin);
        titleLabel.setPosition((float) (Gdx.graphics.getWidth() - titleLabel.getWidth()) / 2 , (float) Gdx.graphics.getHeight() / 2 + titleLabel.getHeight());

        titleFont = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                Gdx.app.debug(TAG, "Received keycode: " + keycode);
                switch (keycode) {
                    case Input.Keys.Z -> {
                        game.changeScreen(EScreen.GAME);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        // Label start = new Label("Start", );
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);

        batch.begin();
        titleLabel.draw(batch, 1.0f);
        batch.end();

        stage.act(delta);
        stage.draw();
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
        stage.dispose();
        batch.dispose();
        titleFont.dispose();
    }
}
