package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.ui.HomeScreen;

public class MyGame extends Game {
	private Stage stage;

	@Override
	public void create() {
		stage = new Stage(new ScreenViewport());
		setScreen(new HomeScreen(this, stage));
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}
}
