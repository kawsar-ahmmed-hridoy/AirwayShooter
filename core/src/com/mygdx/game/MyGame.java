package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ui.HomeScreen;

public class MyGame extends ApplicationAdapter {
	private HomeScreen display;
	public static final int Width=480;
	public static final int Height=720;
	public SpriteBatch batch;

	public void create(){
		display = new HomeScreen();
	}

	public void render(){
		float chk = Gdx.graphics.getDeltaTime();
		display.render(chk);
	}

	public void dispose(){
		display.dispose();
	}
}
