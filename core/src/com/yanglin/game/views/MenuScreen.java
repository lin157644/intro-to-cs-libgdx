package com.yanglin.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.MusicManager;

public class MenuScreen implements Screen {
    private static final String TAG = MenuScreen.class.getSimpleName();
    private IWantToGraduate game;
    private final SpriteBatch batch = new SpriteBatch();
    private final Label gtSign;
    private final Label ltSign;
    private final Stage stage = new Stage(new ExtendViewport(1280, 720), batch);
    private final Array<Actor> menuSelectionList = new Array<>();
    private Label currSel;


    public MenuScreen(IWantToGraduate game) {
        this.game = game;

        FreeTypeFontGenerator generator = game.assetManager.get("fonts/title_font.otf");
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "大延畢";
        parameter.size = 120;

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        TypingLabel titleLabel = new TypingLabel("{SHAKE=1;1;1}大延畢{ENDSHAKE}", skin, "menuTitle");
        // Label titleLabel = new Label("I Want To Graduate", skin, "menuTitle");
        // titleLabel.setPosition((float) (Gdx.graphics.getWidth() - titleLabel.getWidth()) / 2 + 300, (float) Gdx.graphics.getHeight() / 2 + titleLabel.getHeight());

        Label continueLabel = new Label("Continue", skin, "menuSelection");
        continueLabel.setName("continue");
        continueLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameState = GameState.loadState();
                game.changeScreen(EScreen.GAME);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                currSel = continueLabel;
                updateSignPosition();
            }
        });


        Label startLabel = new Label("Start", skin, "menuSelection");
        startLabel.setName("start");
        startLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameState.clearSavedState()) {
                    Gdx.app.log(TAG, "Start the game: Successfully removed old file.");
                }
                game.gameState = GameState.loadState();
                game.changeScreen(EScreen.GAME);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                currSel = startLabel;
                updateSignPosition();
            }
        });

        Label creditLabel = new Label("Credit", skin, "menuSelection");
        creditLabel.setName("credit");
        creditLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                currSel = creditLabel;
                updateSignPosition();
            }
        });


        Label exitLabel = new Label("Exit", skin, "menuSelection");
        exitLabel.setName("exit");
        exitLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                currSel = exitLabel;
                updateSignPosition();
            }
        });

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
        if (GameState.saveExist()) {
            menuSelectionList.add(continueLabel);
        }
        vbox.addActor(new Actor());
        menuSelectionList.add(startLabel, creditLabel, exitLabel);
        for (Actor actor : menuSelectionList) {
            vbox.addActor(actor);
        }

        vbox.space(30f);

        vbox.setPosition((float) (Gdx.graphics.getWidth() - vbox.getWidth()) / 2, (float) Gdx.graphics.getHeight() / 2 + startLabel.getHeight() * 2 + titleLabel.getHeight() / 2 + 90);

        float signOffset = 20f;
        currSel = (Label) menuSelectionList.get(0);
        Vector2 currSelVec = currSel.localToStageCoordinates(new Vector2(0, 0));
        // 第一次取的currSelVec會是VBox的
        gtSign.setPosition(currSelVec.x - currSel.getWidth() / 2 - gtSign.getWidth() - signOffset, currSelVec.y - titleLabel.getHeight() - 60 - currSel.getHeight());
        ltSign.setPosition(currSelVec.x + currSel.getWidth() / 2 + signOffset, currSelVec.y - titleLabel.getHeight() - 60 - currSel.getHeight());

        Image background = new Image((Texture) game.assetManager.get("img/the_wall.jpg"));
        background.setPosition((stage.getWidth() - background.getWidth()) / 2, (stage.getHeight() - background.getHeight()) / 2 - 9);

        stage.addActor(background);
        stage.addActor(vbox);
        stage.addActor(gtSign);
        stage.addActor(ltSign);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                Gdx.app.debug(TAG, "Received keycode: " + keycode);
                switch (keycode) {
                    case Input.Keys.Z -> {
                        switch (currSel.getName()) {
                            case "continue" -> {
                                // Game state already loaded in IWantToGraduate
                                game.gameState = GameState.loadState();
                                game.changeScreen(EScreen.GAME);
                            }
                            case "start" -> {
                                if (GameState.clearSavedState()) {
                                    Gdx.app.log(TAG, "Start the game: Successfully removed old file.");
                                }
                                game.gameState = GameState.loadState();
                                game.changeScreen(EScreen.GAME);
                            }
                            case "credit" -> {
                                // TODO: Credit
                            }
                            case "exit" -> {
                                Gdx.app.exit();
                            }
                        }
                        // if(GameState.saveExist()){
                        // }
                        return true;
                    }
                    case Input.Keys.UP -> {
                        // identity = true: use == to compare
                        game.musicManager.playEffect(MusicManager.Effect.MENU_SELECT);

                        int currSelIndex = menuSelectionList.indexOf(currSel, true);
                        currSelIndex = currSelIndex == 0 ? menuSelectionList.size - 1 : currSelIndex - 1;
                        currSel = (Label) menuSelectionList.get(currSelIndex);
                        updateSignPosition();
                    }
                    case Input.Keys.DOWN -> {
                        game.musicManager.playEffect(MusicManager.Effect.MENU_SELECT);

                        int currSelIndex = menuSelectionList.indexOf(currSel, true);
                        currSelIndex = currSelIndex == menuSelectionList.size - 1 ? 0 : currSelIndex + 1;
                        currSel = (Label) menuSelectionList.get(currSelIndex);
                        updateSignPosition();
                    }
                }
                return false;
            }
        });

    }

    private void updateSignPosition() {
        float signOffset = 20f;
        Vector2 currSelVec = currSel.localToStageCoordinates(new Vector2(0, 0));
        gtSign.setPosition(currSelVec.x - gtSign.getWidth() - signOffset, currSelVec.y);
        ltSign.setPosition(currSelVec.x + currSel.getWidth() + signOffset, currSelVec.y);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        game.musicManager.setBGM(MusicManager.BGM.MENU, true);
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
