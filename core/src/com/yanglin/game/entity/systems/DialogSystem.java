package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.Ending;
import com.yanglin.game.views.GameScreen;

public class DialogSystem extends EntitySystem implements KeyInputListener, PlayerInteractionSystem.PlayerInteractionListener, TimeSystem.TimeSystemListener {
    private static final String TAG = DialogSystem.class.getSimpleName();
    private final IWantToGraduate game;
    private final Stage stage;
    private Array<String> currentDialog;
    private Boolean inDialog = false;
    private TypingLabel dialogLabel;
    private Image dialogBackground;
    GameScreen gameScreen;
    private Image transitionImg;

    public DialogSystem(IWantToGraduate game, Stage stage, GameScreen gameScreen) {
        // Initialize dialog
        this.game = game;
        this.stage = stage;
        this.gameScreen = gameScreen;

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        dialogBackground = new Image((Texture) game.assetManager.get("ui/dialog_box.png"));

        dialogBackground.setPosition((stage.getWidth() - dialogBackground.getWidth()) / 2, 0);

        dialogLabel = new TypingLabel("", skin, "dialogLabel");
        dialogLabel.setX(dialogBackground.getX() + 80);
        setDialogLabelY();

        Group dialogGroup = new Group();
        dialogGroup.addActor(dialogBackground);
        dialogGroup.addActor(dialogLabel);
        // dialogGroup.setVisible(false);

        dialogLabel.setTypingListener(new TypingAdapter() {
            @Override
            public void event(String event) {
                switch (event) {
                    case "stopDialogEffect" -> {
                        game.musicManager.stopDialog();
                        Gdx.app.log(TAG, "Dialog effect stopped");
                    }
                    case "playDialogEffect" -> {
                        if (gameScreen.getPlayEffect()) {
                            game.musicManager.playDialog();
                        }
                        Gdx.app.log(TAG, "Dialog effect played");
                    }
                    default -> {
                        Gdx.app.log(TAG, "Unknown event in dialog.");
                    }
                }
            }
        });

        transitionImg = new Image((Texture) game.assetManager.get("img/transition.png"));
        transitionImg.setVisible(false);

        stage.addActor(transitionImg);
        stage.addActor(dialogGroup);
    }

    public Boolean getInDialog() {
        return inDialog;
    }

    @Override
    public void onDialog(String text, boolean withTransition) {
        inDialog = true;
        if(withTransition){
            // TODO: Use action pool
            transitionImg.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(0.25f), Actions.fadeOut(0.25f), Actions.visible(false)));
            dialogBackground.addAction(Actions.sequence(Actions.visible(false), Actions.delay(0.6f), Actions.visible(true)));
        }
        setDialog(text, withTransition);
    }

    public void setDialog(String text, boolean withTransition) {
        currentDialog = new Array<>(text.replace("{BR}", "\n").split("\\{NEXT}"));
        currentDialog.reverse();
        if (currentDialog.size > 0) {
            if(withTransition)
                dialogLabel.setText(" {WAIT=0.65}{EVENT=playDialogEffect}" + currentDialog.pop());
            else
                dialogLabel.setText(currentDialog.pop());
            setDialogLabelY();
            dialogLabel.restart();
            if (gameScreen.getPlayEffect() && !withTransition) {
                game.musicManager.playDialog();
            }
        }
    }

    private void setDialogLabelY() {
        dialogLabel.setY(dialogBackground.getHeight() - dialogLabel.getHeight() - 100);
    }

    @Override
    public void update(float deltaTime) {
        // Update
        // Change the menu to render
        if (inDialog) {
            if (dialogLabel.hasEnded()) {
                game.musicManager.stopDialog();
            }
            // if(dialogLabel)
            stage.act(deltaTime);
            stage.draw();
        }
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        // Dialog system must block the input before dialog ends
        if (inDialog) {
            switch (keycode) {
                case Input.Keys.Z -> {
                    if (currentDialog.size > 0) {
                        dialogLabel.setText(currentDialog.pop());
                        setDialogLabelY();
                    } else {
                        inDialog = false;
                        game.musicManager.stopDialog();
                    }
                    return true;
                }
                case Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT -> {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        if (inDialog) {
            switch (keycode) {
                case Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT -> {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }

    @Override
    public void onMonthUpdate(int month) {
    }

    @Override
    public void onDayUpdate(int day) {
        // TODO: Check hunger
        if (game.gameState.hunger > 50) {
            game.ending = Ending.HUNGER;
            game.changeScreen(EScreen.BAD_END);
        }
    }
}
