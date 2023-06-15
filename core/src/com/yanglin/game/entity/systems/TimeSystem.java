package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.yanglin.game.GameState;
import com.yanglin.game.IWantToGraduate;
import com.yanglin.game.MusicManager;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.Ending;
import com.yanglin.game.views.GameScreen;

public class TimeSystem extends IntervalSystem {
    private static String TAG = TimeSystem.class.getSimpleName();
    private Boolean isPaused;
    private GameState gameState;
    private GameScreen gameScreen;
    public int hungerAccumulator = 0;
    private Array<TimeSystemListener> listeners = new Array<>();
    private IWantToGraduate game;

    public TimeSystem(IWantToGraduate game, GameScreen gameScreen) {
        super(1); // In second
        this.game = game;
        this.gameScreen = gameScreen;
        this.gameState = game.gameState;
    }

    @Override
    protected void updateInterval() {
        if (!gameScreen.isPaused) {
            switch (gameState.month) {
                case 4, 6 -> {
                    // 30 days
                    if (gameState.date == 30) {
                        if (gameState.month == 6) {
                            Gdx.app.log(TAG, "Time over!");
                            game.ending = Ending.TIME_OUT;
                            game.changeScreen(EScreen.BAD_END);
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
                        game.musicManager.setBGM(MusicManager.BGM.GAME_JUNE, true);
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
