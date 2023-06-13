package com.yanglin.game.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;

public class MenuScreen implements Screen {
    private static final String TAG = MenuScreen.class.getSimpleName();
    private IWantToGraduate game;
    private final SpriteBatch batch = new SpriteBatch();
    private final Label gtSign;
    private final Label ltSign;
    private final Stage stage = new Stage(new ExtendViewport(1280, 720), batch);
    private final Array<Actor> menuSelectionList = new Array<>();
    private int currSel = 0;

    public MenuScreen(IWantToGraduate game) {
        this.game = game;

        FreeTypeFontGenerator generator = game.assetManager.get("fonts/title_font.otf");
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "大延畢";
        parameter.size = 120;

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        TypingLabel titleLabel = new TypingLabel("{SHAKE=1;1;1}I Want To Graduate{ENDSHAKE}", skin, "menuTitle");
        // Label titleLabel = new Label("I Want To Graduate", skin, "menuTitle");
        // titleLabel.setPosition((float) (Gdx.graphics.getWidth() - titleLabel.getWidth()) / 2 + 300, (float) Gdx.graphics.getHeight() / 2 + titleLabel.getHeight());

        Label continueLabel = new Label("Continue", skin, "menuSelection");
        Label startLabel = new Label("Start", skin, "menuSelection");
        Label creditLabel = new Label("Credit", skin, "menuSelection");
        Label exitLabel = new Label("Exit", skin, "menuSelection");

        // continueLabel.addAction(Actions.sequence(Actions.delay(2), Actions.fadeIn(1f)));
        // startLabel.addAction(Actions.sequence(Actions.delay(2.5f), Actions.fadeIn(1f)));
        // creditLabel.addAction(Actions.sequence(Actions.delay(3.5f), Actions.fadeIn(1f)));
        // exitLabel.addAction(Actions.sequence(Actions.delay(4f), Actions.fadeIn(1f)));

        gtSign = new Label(">", skin, "menuSelection");
        ltSign = new Label("<", skin, "menuSelection");
        gtSign.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(
                Actions.moveBy(10, 0, 0.8f),
                Actions.moveBy(-10, 0, 0.8f))));

        ltSign.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(
                Actions.moveBy(-10, 0, 0.8f),
                Actions.moveBy(10, 0, 0.8f))));

        VerticalGroup vbox = new VerticalGroup();

        vbox.addActor(titleLabel);
        if (GameState.saveExist()){
            menuSelectionList.add(continueLabel);
        }
        menuSelectionList.add(startLabel, creditLabel, exitLabel);
        for(Actor actor : menuSelectionList) {
            vbox.addActor(actor);
        }

        vbox.space(30f);
        vbox.setPosition((float) (Gdx.graphics.getWidth() - vbox.getWidth()) / 2 , (float) Gdx.graphics.getHeight() / 2 + startLabel.getHeight() * 2 + titleLabel.getHeight()/2 + 60);

        float signOffset = 20f;
        Actor currSelActor = menuSelectionList.get(currSel);
        Vector2 currSelVec = currSelActor.localToStageCoordinates(new Vector2(0,0));
        // 第一次取的currSelVec會是VBox的
        gtSign.setPosition(currSelVec.x - currSelActor.getWidth() / 2 - gtSign.getWidth() - signOffset, currSelVec.y - titleLabel.getHeight() -30 - currSelActor.getHeight());
        ltSign.setPosition(currSelVec.x + currSelActor.getWidth() / 2 + signOffset, currSelVec.y - titleLabel.getHeight() -30 - currSelActor.getHeight());

        stage.addActor(vbox);
        stage.addActor(gtSign);
        stage.addActor(ltSign);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                Gdx.app.debug(TAG, "Received keycode: " + keycode);
                switch (keycode) {
                    case Input.Keys.Z -> {
                        if(GameState.saveExist()){
                            switch (currSel){
                                case 0 -> {
                                    game.changeScreen(EScreen.GAME);
                                }
                                case 1 -> {
                                    GameState.clearSavedState();
                                    game.gameState = GameState.loadState();
                                }
                                case 2 -> {
                                    // TODO: Credit
                                }
                                case 3 -> {
                                    Gdx.app.exit();
                                }
                            }
                        } else {
                            switch (currSel) {
                                case 0 -> {
                                    game.changeScreen(EScreen.GAME);
                                }
                                case 1 -> {
                                    // TODO: Credit
                                }
                                case 2 -> {
                                    Gdx.app.exit();
                                }
                            }
                        }
                        return true;
                    }
                    case Input.Keys.UP -> {
                        currSel = currSel == 0 ? menuSelectionList.size -1 : currSel - 1;
                        updateSignPosition();
                    }
                    case Input.Keys.DOWN -> {
                        currSel = currSel == menuSelectionList.size - 1 ? 0 : currSel + 1;
                        updateSignPosition();
                    }
                }
                return false;
            }
        });

    }

    private void updateSignPosition(){
        float signOffset = 20f;
        Actor currSelActor = menuSelectionList.get(currSel);
        Vector2 currSelVec = currSelActor.localToStageCoordinates(new Vector2(0, 0));
        gtSign.setPosition(currSelVec.x - gtSign.getWidth() - signOffset, currSelVec.y);
        ltSign.setPosition(currSelVec.x + currSelActor.getWidth() + signOffset, currSelVec.y);
        System.out.println(currSelVec.x);
        System.out.println(currSelVec.y);
        System.out.println( currSelActor.getHeight());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
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
    }
}
