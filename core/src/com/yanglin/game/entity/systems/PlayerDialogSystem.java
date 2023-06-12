package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.yanglin.game.input.GameInputProcessor;
import com.yanglin.game.input.KeyInputListener;

public class PlayerDialogSystem extends EntitySystem implements KeyInputListener, PlayerInteractionSystem.PlayerInteractionListener, TimeSystem.TimeSystemListener {
    // Player <-> NPC
    // Player get item
    // Time pass
    //...
    public PlayerDialogSystem(){
        // Initialize dialog
    }

    @Override
    public boolean keyDown(GameInputProcessor gameInputProcessor, int keycode) {
        // If
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

    }

    @Override
    public void onMonthUpdate(int month) {}

    @Override
    public void onDayUpdate(int day) {
        // TODO: Check hunger
    }
}
