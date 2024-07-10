package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen {
    private final Stage stage;
    private final Image instructionImage;
    private final Image musicButton;
    private boolean musicOn;
    public MenuScreen(Stage stage){
        this.stage = stage;

        //Creating MenuBackground.
        Texture menuBackgroundTexture = new Texture(Gdx.files.internal("background2.png"));
        Image backgroundImage = new Image(menuBackgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        //Accessing images for button textures.
        Texture instructionTexture = new Texture(Gdx.files.internal("instruction.png"));
        Texture instructionImageTexture = new Texture(Gdx.files.internal("background.png"));
        Texture rocket1Texture = new Texture(Gdx.files.internal("blue_ship.png"));
        Texture rocket2Texture = new Texture(Gdx.files.internal("red_ship.png"));
        Texture rocket3Texture = new Texture(Gdx.files.internal("green_ship.png"));
        Texture rocket4Texture = new Texture(Gdx.files.internal("orange_ship.png"));
        Texture exitTexture = new Texture(Gdx.files.internal("exit.png"));
        Texture playTexture = new Texture(Gdx.files.internal("play.png"));
        Texture musicOnTexture = new Texture(Gdx.files.internal("!music.png"));
        Texture bestScoresTexture = new Texture(Gdx.files.internal("highScore.png"));

        //Creating buttons.
        Image instructionButton = new Image(instructionTexture);
        instructionButton.setPosition(52, 580);
        instructionButton.setHeight(70);
        instructionButton.setWidth(70);
        stage.addActor(instructionButton);

        Image[] rocketButtons = new Image[4];
        rocketButtons[0] = new Image(rocket1Texture);
        rocketButtons[0].setPosition(74, 152);
        rocketButtons[1] = new Image(rocket2Texture);
        rocketButtons[1].setPosition(182, 152);
        rocketButtons[2] = new Image(rocket3Texture);
        rocketButtons[2].setPosition(127, 80);
        rocketButtons[3] = new Image(rocket4Texture);
        rocketButtons[3].setPosition(127, 215);
        for(int i=0;i<4;i++){
            rocketButtons[i].setWidth(40);
            rocketButtons[i].setHeight(40);
        }
        stage.addActor(rocketButtons[0]);
        stage.addActor(rocketButtons[1]);
        stage.addActor(rocketButtons[2]);
        stage.addActor(rocketButtons[3]);

        Image exitButton = new Image(exitTexture);
        exitButton.setPosition(448, 80);
        exitButton.setWidth(180);
        exitButton.setHeight(90);
        stage.addActor(exitButton);

        Image playButton = new Image(playTexture);
        playButton.setPosition(385, 220);
        playButton.setHeight(300);
        playButton.setWidth(300);
        stage.addActor(playButton);

        musicButton = new Image(musicOnTexture);
        musicButton.setPosition(1015, 606);
        musicButton.setHeight(40);
        musicButton.setWidth(40);
        stage.addActor(musicButton);
        musicOn = true;//Initial state.

        Image bestScoresButton = new Image(bestScoresTexture);
        bestScoresButton.setPosition(900, 200);
        bestScoresButton.setHeight(30);
        bestScoresButton.setWidth(120);
        stage.addActor(bestScoresButton);

        instructionImage = new Image(instructionImageTexture);
        instructionImage.setPosition(140, 90);
        instructionImage.setWidth(820);
        instructionImage.setHeight(550);
        instructionImage.setVisible(false);//Initially not shown.
        stage.addActor(instructionImage);

        instructionButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                instructionImage.setVisible(!instructionImage.isVisible());
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                toggleMusic();
            }
        });
    }

    private void toggleMusic(){
        if(musicOn){
            musicButton.setDrawable(new Image(new Texture(Gdx.files.internal("!music.png"))).getDrawable());
            musicOn = false;
        }else{
            musicButton.setDrawable(new Image(new Texture(Gdx.files.internal("music.png"))).getDrawable());
            musicOn = true;
        }
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }

}