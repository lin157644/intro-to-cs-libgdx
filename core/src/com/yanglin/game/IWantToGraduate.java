package com.yanglin.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.yanglin.game.entity.MapManager;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.LoadingScreen;
import com.yanglin.game.views.GameScreen;
import com.yanglin.game.views.MenuScreen;

import java.util.EnumMap;

public class IWantToGraduate extends Game {
    // ApplicationAdapter is more basic than Game
    // It allows fancier transition, but Game is sufficient for us.
    private static final String TAG = Game.class.getSimpleName();
    public Screen currentScreen;
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private final EnumMap<EScreen, Screen> screenMap = new EnumMap<>(EScreen.class);
    public GameAssetManager assetManager = new GameAssetManager();
    public MapManager mapManager = new MapManager();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        changeScreen(EScreen.LOADING);
    }

    public void changeScreen(EScreen s) {
        if (currentScreen != null) {
            currentScreen.dispose();
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
