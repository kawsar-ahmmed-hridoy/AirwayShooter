package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGame;

public class MenuScreen implements Screen {
    private final Stage stage;
    private int selectedShip = 0;
    private final Image instructionImage;
    private final Image musicButton;
    private boolean musicOn;


    public MenuScreen(MyGame game, Stage stage) {
        this.stage = stage;

        Texture menuBackgroundTexture = new Texture(Gdx.files.internal("background2.png"));
        Image backgroundImage = new Image(menuBackgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Texture rocket1Texture = new Texture(Gdx.files.internal("blue_ship.png"));
        Texture rocket2Texture = new Texture(Gdx.files.internal("red_ship.png"));
        Texture rocket3Texture = new Texture(Gdx.files.internal("green_ship.png"));
        Texture rocket4Texture = new Texture(Gdx.files.internal("orange_ship.png"));

        Image[] rocketButtons = new Image[4];
        rocketButtons[0] = new Image(rocket1Texture);
        rocketButtons[0].setPosition(74, 152);
        rocketButtons[1] = new Image(rocket2Texture);
        rocketButtons[1].setPosition(182, 152);
        rocketButtons[2] = new Image(rocket3Texture);
        rocketButtons[2].setPosition(127, 80);
        rocketButtons[3] = new Image(rocket4Texture);
        rocketButtons[3].setPosition(127, 215);

        for (int i = 0; i < 4; i++) {
            rocketButtons[i].setWidth(40);
            rocketButtons[i].setHeight(40);
            final int index = i;
            rocketButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedShip = index;
                }
            });
            stage.addActor(rocketButtons[i]);
        }

        Texture instructionTexture = new Texture(Gdx.files.internal("instruction.png"));
        Texture instructionImageTexture = new Texture(Gdx.files.internal("background.png"));
        Texture exitTexture = new Texture(Gdx.files.internal("exit.png"));
        Texture playTexture = new Texture(Gdx.files.internal("play.png"));
        Texture musicOnTexture = new Texture(Gdx.files.internal("!music.png"));
        Texture bestScoresTexture = new Texture(Gdx.files.internal("highScore.png"));

        Image instructionButton = new Image(instructionTexture);
        instructionButton.setPosition(52, 580);
        instructionButton.setSize(70, 70);
        stage.addActor(instructionButton);

        Image playButton = new Image(playTexture);
        playButton.setPosition(385, 220);
        playButton.setSize(300, 300);
        stage.addActor(playButton);

        Image exitButton = new Image(exitTexture);
        exitButton.setPosition(448, 80);
        exitButton.setSize(180, 90);
        stage.addActor(exitButton);

        musicButton = new Image(musicOnTexture);
        musicButton.setPosition(900, 590);
        musicButton.setSize(60, 60);
        stage.addActor(musicButton);
        musicOn = true;

        Image bestScoresButton = new Image(bestScoresTexture);
        bestScoresButton.setPosition(900, 200);
        bestScoresButton.setSize(120, 30);
        stage.addActor(bestScoresButton);

        instructionImage = new Image(instructionImageTexture);
        instructionImage.setPosition(140, 90);
        instructionImage.setSize(820, 550);
        instructionImage.setVisible(false);
        stage.addActor(instructionImage);

        instructionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instructionImage.setVisible(!instructionImage.isVisible());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleMusic();
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, selectedShip));
            }
        });
    }

    private void toggleMusic() {
        if (musicOn) {
            musicButton.setDrawable(new Image(new Texture(Gdx.files.internal("!music.png"))).getDrawable());
            musicOn = false;
        } else {
            musicButton.setDrawable(new Image(new Texture(Gdx.files.internal("music.png"))).getDrawable());
            musicOn = true;
        }
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
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
    }
}

