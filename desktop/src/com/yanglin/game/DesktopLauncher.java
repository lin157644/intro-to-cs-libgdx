package com.yanglin.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yanglin.game.IWantToGraduate;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("I Want To Graduate");
		config.setForegroundFPS(60);
		config.setWindowedMode(1280, 768);
		new Lwjgl3Application(new IWantToGraduate(), config);
	}
}
