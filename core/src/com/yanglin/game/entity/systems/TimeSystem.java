package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.yanglin.game.GameState;

public class TimeSystem extends IntervalSystem {
    private static String TAG = TimeSystem.class.getSimpleName();
    private Boolean isPaused;
    private GameState gameState;
    public int hungerAccumulator = 0;
    private Array<TimeSystemListener> listeners = new Array<>();

    public TimeSystem(GameState gameState, Boolean isPaused) {
        super(10000);
        this.isPaused = isPaused;
        this.gameState = gameState;
    }

    @Override
    protected void updateInterval() {
        if (!isPaused) {
            switch (gameState.month) {
                case 4, 6 -> {
                    // 30 days
                    if (gameState.date == 30) {
                        if (gameState.month == 6) {
                            Gdx.app.log(TAG, "Time over!");
                        }
                        gameState.month += 1;
                        gameState.date = 1;
                        notifyMonthUpdate(gameState.month);
                        notifyDayUpdate(gameState.date);
                    } else {
                        gameState.date += 1;
                        notifyDayUpdate(gameState.date);
                    }
                }
                case 5 -> {
                    // 31 days
                    if (gameState.date == 31) {
                        gameState.month += 1;
                        gameState.date = 1;
                        notifyMonthUpdate(gameState.month);
                        notifyDayUpdate(gameState.date);
                    } else {
                        gameState.date += 1;
                        notifyDayUpdate(gameState.date);
                    }
                }
            }
            gameState.hunger += 1;
        }
    }

    public void addTimeSystemListener(TimeSystemListener listener) {
        listeners.add(listener);
    }

    private void notifyMonthUpdate(int month) {
        for (TimeSystemListener listener : listeners) {
            listener.onMonthUpdate(month);
        }
    }

    private void notifyDayUpdate(int day) {
        for (TimeSystemListener listener : listeners) {
            listener.onDayUpdate(day);
        }
    }

    interface TimeSystemListener {
        void onMonthUpdate(int month);
        void onDayUpdate(int day);
    }
}
