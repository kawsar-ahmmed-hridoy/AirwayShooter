package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGame;

public class GameOverScreen implements Screen {
    private final MyGame game;
    private final Stage stage;

    private final Texture gameOverTexture;
    private int selectedShip;

    public GameOverScreen(MyGame game, int score) {
        this.game = game;
        this.stage = new Stage();

        gameOverTexture = new Texture(Gdx.files.internal("gameOver.png"));
        Image gameOverImage = new Image(gameOverTexture);
        gameOverImage.setFillParent(true);
        stage.addActor(gameOverImage);

        Texture restartTexture = new Texture(Gdx.files.internal("restart.png"));
        Image restartButton = new Image(restartTexture);
        restartButton.setSize(120, 120);
        restartButton.setPosition(492, 315);
        stage.addActor(restartButton);

        Texture menuTexture = new Texture(Gdx.files.internal("menuButton.png"));
        Image menuButton = new Image(menuTexture);
        menuButton.setSize(90, 115);
        menuButton.setPosition(905, 100);
        stage.addActor(menuButton);

        Texture quitTexture = new Texture(Gdx.files.internal("quit.png"));
        Image quitButton = new Image(quitTexture);
        quitButton.setSize(90, 90);
        quitButton.setPosition(110, 125);
        stage.addActor(quitButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restartGame();
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToMenuScreen();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quitGame();
            }
        });

        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);

        Label scoreLabel = new Label("SCORE: " + score, labelStyle);
        scoreLabel.setFontScale(1);
        scoreLabel.setPosition(503,682);
        stage.addActor(scoreLabel);

        FileHandle file = Gdx.files.local("bestScores.txt");
        int bestScore = 0;
        if (file.exists()) {
            try {
                bestScore = Integer.parseInt(file.readString().trim());
            } catch (NumberFormatException e) {
//                bestScore = 0;
            }
        }

        if (score > bestScore) {
            bestScore = score;
            file.writeString(Integer.toString(bestScore), false);
        }

        Label bestScoreLabel = new Label("BEST SCORE: " + bestScore, labelStyle);
        bestScoreLabel.setFontScale(1);
        bestScoreLabel.setPosition(60,550);
        stage.addActor(bestScoreLabel);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        gameOverTexture.dispose();
    }

    private void goToMenuScreen() {
        game.setScreen(new MenuScreen(game, stage));
    }

    private void restartGame() {
        game.setScreen(new GameScreen(game, selectedShip));
    }

    private void quitGame() {
        Gdx.app.exit();
    }
}