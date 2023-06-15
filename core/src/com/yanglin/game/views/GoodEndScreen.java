package com.yanglin.game.views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yanglin.game.IWantToGraduate;

public class GoodEndScreen implements Screen {
    private static String TAG = BadEndScreen.class.getSimpleName();
    private IWantToGraduate game;
    private Stage stage = new Stage();

    public GoodEndScreen(IWantToGraduate game) {
        this.game = game;

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        Image happyMan = game.assetManager.get("img/happy_man.png");
        Image cert = game.assetManager.get("img/cert.png");

        // Image background = game.assetManager.get("img/good_end_bg.png");
        Image spinner = game.assetManager.get("img/spinner.png");
        // spinner.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(90f, 10.f)));

        Label title = new Label("恭喜畢業", skin, "goodEndTitle");

        // spinner.setPosition((stage.getWidth() + spinner.getWidth()) / 2, (stage.getHeight() + spinner.getHeight()) / 2);
        title.setPosition((stage.getWidth() + title.getWidth()) / 2, (stage.getHeight() + title.getHeight()) / 2);
        // background.setPosition((stage.getWidth() + background.getWidth()) / 2, (stage.getHeight() + background.getHeight()) / 2);
        happyMan.setPosition((stage.getWidth() + happyMan.getWidth()) / 2, (stage.getHeight() + happyMan.getHeight()) / 2);
        cert.setPosition((stage.getWidth() + cert.getWidth()) / 2, (stage.getHeight() + cert.getHeight()) / 2);

        // stage.addActor(spinner);
        stage.addActor(title);
        stage.addActor(happyMan);
        stage.addActor(cert);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(242f, 190f, 34f, 1f);
        stage.act();
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
    }
}
