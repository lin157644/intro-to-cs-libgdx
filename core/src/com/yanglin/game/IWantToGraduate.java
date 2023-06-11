package com.yanglin.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.yanglin.game.entity.EntityEngine;
import com.yanglin.game.views.EScreen;
import com.yanglin.game.views.LoadingScreen;
import com.yanglin.game.views.GameScreen;
import com.yanglin.game.views.MenuScreen;

import java.util.EnumMap;

public class IWantToGraduate extends Game {
    // ApplicationAdapter is more basic than Game
    // Allow fancier transition
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private final EnumMap<EScreen, Screen> screenMap = new EnumMap<>(EScreen.class);
    public MainAssetManager assetManager = new MainAssetManager();
    public Screen currentScreen;

    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        changeScreen(EScreen.LOADING);
    }

    public void changeScreen(EScreen s) {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = screenMap.get(s);
        if (currentScreen == null) {
            try {
                // Very elegant Ref: Mystic garden
                screenMap.put(s, (Screen) ClassReflection.getConstructor(s.getScreenType(), IWantToGraduate.class).newInstance(this));
                currentScreen = screenMap.get(s);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Could not create screen of type " + s, e);
            }
        }
        setScreen(currentScreen);
    }
}
