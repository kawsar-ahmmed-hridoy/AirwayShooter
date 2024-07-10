package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGame;

public class GameScreen implements Screen {
    private final MyGame game;
    private final Stage stage;
    private final Texture backgroundTexture1;
    private final Texture backgroundTexture2;
    private final Texture spaceshipTexture;
    private final Image spaceship;
    private float backgroundY1;
    private float backgroundY2;

    public GameScreen(MyGame game){
        this.game = game;
        this.stage = new Stage();

        this.backgroundTexture1 = new Texture(Gdx.files.internal("bgm5.jpg"));
        this.backgroundTexture2 = new Texture(Gdx.files.internal("bgm5.jpg"));
        this.spaceshipTexture = new Texture(Gdx.files.internal("blue_ship.png"));
        this.spaceship = new Image(spaceshipTexture);
        spaceship.setPosition(400, 300);
        stage.addActor(spaceship);
        backgroundY1 = 0;
        backgroundY2 = backgroundTexture1.getHeight();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Updating background position.
        backgroundY1 -= 200 * delta;
        backgroundY2 -= 200 * delta;

        // Resetting position to create a seamless bg.
        if (backgroundY1 <= -backgroundTexture1.getHeight()) {
            backgroundY1 = backgroundTexture2.getHeight();
        }
        if (backgroundY2 <= -backgroundTexture2.getHeight()) {
            backgroundY2 = backgroundTexture1.getHeight();
        }
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture1, 0, backgroundY1);
        stage.getBatch().draw(backgroundTexture2, 0, backgroundY2);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture1.dispose();
        backgroundTexture2.dispose();
        spaceshipTexture.dispose();
    }
}