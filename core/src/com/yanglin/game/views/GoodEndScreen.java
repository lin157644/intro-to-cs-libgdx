package com.yanglin.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.MusicManager;

public class GoodEndScreen implements Screen {
    private static String TAG = BadEndScreen.class.getSimpleName();
    private IWantToGraduate game;
    private Stage stage = new Stage();

    private Color bgColor = new Color(231 / 256f, 203 / 256f, 158 / 256f, 1f); // rgb(231, 203, 158)

    public GoodEndScreen(IWantToGraduate game) {
        this.game = game;
        Gdx.input.setInputProcessor(stage);

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        Image happyMan = new Image((Texture) game.assetManager.get("img/happy_man.png"));
        Image cert = new Image((Texture) game.assetManager.get("img/cert.png"));

        // Image background = game.assetManager.get("img/good_end_bg.png");
        Image spinner = new Image((Texture) game.assetManager.get("img/spinner.png"));
        spinner.setSize(2000, 2000);
        spinner.setOrigin(spinner.getWidth() / 2, spinner.getHeight() / 2);
        spinner.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(90f, 10.f)));

        Label title = new Label("恭喜畢業", skin, "goodEndTitle");

        spinner.setPosition((stage.getWidth() - spinner.getWidth()) / 2, (stage.getHeight() - spinner.getHeight()) / 2);
        title.setPosition((stage.getWidth() - title.getWidth()) / 2, (stage.getHeight() + title.getHeight()) / 2);
        // background.setPosition((stage.getWidth() + background.getWidth()) / 2, (stage.getHeight() + background.getHeight()) / 2);

        float happyManScale = -0.2f;
        happyMan.scaleBy(happyManScale);
        happyMan.setPosition((stage.getWidth() - happyMan.getWidth() * (1 + happyManScale)) / 2, (stage.getHeight() - happyMan.getHeight() * (1 + happyManScale)) / 2 - 230);

        float certScale = -0.5f;
        cert.scaleBy(certScale);
        cert.setPosition((stage.getWidth() - cert.getWidth() * (1 + certScale)) / 2 + 15 , (stage.getHeight() - cert.getHeight() * (1 + certScale)) / 2);

        stage.addActor(spinner);
        stage.addActor(title);
        stage.addActor(happyMan);
        stage.addActor(cert);

        // game.musicManager.setBGM(MusicManager.BGM.GOOD_END, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(bgColor);
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
        game.musicManager.stopBGM();
        stage.dispose();
    }
}
