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
    public MapManager mapManager;
    public GameState gameState;
    public MusicManager musicManager;
    public Ending ending;
    public Cursor cursor;
    public Cursor cursor_pressed;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_NONE);

        // if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        //     GameState.clearSavedState();

        gameState = GameState.loadState();

        // if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        //     GameState.saveState(gameState);

        musicManager = new MusicManager(this);

        mapManager = new MapManager(this);

        changeScreen(EScreen.LOADING);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Pixmap pixmap = new Pixmap(Gdx.files.internal("sprites/cursor_3.png"));
        Pixmap pixmap_pressed = new Pixmap(Gdx.files.internal("sprites/cursor_4.png"));
        // Set hotspot to the middle of it (0,0 would be the top-left corner)
        int xHotspot = 3, yHotspot = 5;
        cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        cursor_pressed = Gdx.graphics.newCursor(pixmap_pressed, xHotspot, yHotspot);
        pixmap.dispose(); // We don't need the pixmap anymore
        pixmap_pressed.dispose();
        Gdx.graphics.setCursor(cursor);
    }

    public void setEnding(Ending ending) {
        this.ending = ending;
    }

    public void changeScreen(EScreen s) {
        musicManager.stopBGM();
        musicManager.stopDialog();
        if (currentScreen != null) {
            currentScreen.dispose();
            screenMap.put(s, null);
            Gdx.app.debug(TAG, "Disposed screen " + currentScreen.getClass().getSimpleName());
        }
        currentScreen = screenMap.get(s);
        if (currentScreen == null) {
            try {
                // Very elegant way of initializing new screens. Ref: Mystic garden
                Gdx.app.debug(TAG, "Create screen " + s.name());
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
