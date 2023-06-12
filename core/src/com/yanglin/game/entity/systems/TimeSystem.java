package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IntervalSystem;
import com.yanglin.game.GameState;

public class TimeSystem extends IntervalSystem {
    private Boolean isPaused;
    private GameState gameState;
    public TimeSystem(GameState gameState, Boolean isPaused){
        super(10000);
        this.isPaused = isPaused;
        this.gameState = gameState;
    }

    @Override
    protected void updateInterval() {
        if (!isPaused){
            // TODO: Should convert and update time in game state
            switch (gameState.month) {

            }
        }
    }
}
