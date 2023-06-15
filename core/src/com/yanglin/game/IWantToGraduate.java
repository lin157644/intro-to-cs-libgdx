package com.yanglin.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.Ending;

import java.util.EnumMap;

public class IWantToGraduate extends Game {
    // ApplicationAdapter is more basic than Game
    // It allows fancier transition, but Game is sufficient for us.
    private static final String TAG = IWantToGraduate.class.getSimpleName();
    public Screen currentScreen;
    private final EnumMap<EScreen, Screen> screenMap = new EnumMap<>(EScreen.class);
    public GameAssetManager assetManager = new GameAssetManager();
    public MapManager mapManager = new MapManager();
    public GameState gameState;
    public Ending ending;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
            GameState.clearSavedState();

        gameState = GameState.loadState();

        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
            GameState.saveState(gameState);

        mapManager.setCurrentMap(gameState.map, 24, 20);
        changeScreen(EScreen.LOADING);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
    }

    public void setEnding(Ending ending) {
        this.ending = ending;
    }

    public void changeScreen(EScreen s) {
        if (currentScreen != null) {
            currentScreen.dispose();
            screenMap.put(s, null);
            Gdx.app.debug(TAG, "Disposed screen " + currentScreen.getClass().getSimpleName());
        }
        currentScreen = screenMap.get(s);
        if (currentScreen == null) {
            try {
                // Very elegant way of initializing new screens. Ref: Mystic garden
                screenMap.put(s, (Screen) ClassReflection.getConstructor(s.getScreenType(), IWantToGraduate.class).newInstance(this));
                currentScreen = screenMap.get(s);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Could not create screen of type " + s, e);
            }
        }
        setScreen(currentScreen);
        Gdx.app.debug(TAG, "Successfully change to Screen " + s.name());
    }
}
