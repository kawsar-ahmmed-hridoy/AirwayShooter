package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

//Application needs to be started with the X_startOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);//It will run at a maximum of 60 frames per second.
		config.setTitle("Airway Shooter");
		config.setWindowedMode(1080,720);
		config.setResizable(false);
		new Lwjgl3Application(new MyGame(), config);
	}
}
