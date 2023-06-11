package com.yanglin.game.views;

import com.badlogic.gdx.Screen;

public enum EScreen {
    MENU(MenuScreen.class), GAME(GameScreen.class), LOADING(LoadingScreen.class), GOOD_END(GoodEndScreen.class), BAD_END(BadEndScreen.class);

    private final Class<? extends Screen> screen;

    EScreen(Class<? extends Screen> screen) {
        this.screen = screen;
    }

    public Class<? extends Screen> getScreenType() {
        return screen;
    }

}
