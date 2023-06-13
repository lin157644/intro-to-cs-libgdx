package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.Ending;

public class DialogSystem extends EntitySystem implements KeyInputListener, PlayerInteractionSystem.PlayerInteractionListener, TimeSystem.TimeSystemListener {
    // Player <-> NPC
    // Player get item
    // Time pass
    //...
    private final IWantToGraduate game;
    private final Stage stage;
    private Array<String> currentDialog;
    private Boolean inDialog = false;
    private TypingLabel typingLabel;

    public DialogSystem(IWantToGraduate game, Stage stage) {
        // Initialize dialog
        this.game = game;
        this.stage = stage;

        Skin skin = game.assetManager.get("ui/skins/title_skin.json");

        // Dialog background

        currentDialog = new Array<>();
        currentDialog.add("Test dialog 1");
        currentDialog.add("Test dialog 2");

        ;

        typingLabel = new TypingLabel("Hello world!", skin);
        stage.addActor(typingLabel);
    }

    @Override
    public void update(float deltaTime) {
        // Update
        // Change the menu to render
        if (inDialog) {
            stage.act(deltaTime);
            stage.draw();
        }
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        if(inDialog){
            switch (keycode) {
                case Input.Keys.L -> {
                    if(currentDialog.size > 0) {
                        typingLabel.setText(currentDialog.pop());
                    } else {
                        inDialog = false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(GameInputProcessor gameInputProcessor, int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(GameInputProcessor gameInputProcessor, char character) {
        return false;
    }

    @Override
    public void triggerEvent(EventType eventType) {
        switch (eventType) {
            case GIFT2PROF -> {
                this.inDialog = true;
            }
        }
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
