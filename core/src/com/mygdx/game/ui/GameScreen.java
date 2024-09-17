package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.MyGame;
import com.mygdx.game.entities.Asteroids;
import com.mygdx.game.entities.Bullets;
import com.mygdx.game.entities.BonusFood;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {
    private final MyGame game;
    private final Stage stage;
    private final Texture backgroundTexture1;
    private final Texture backgroundTexture2;
    private final Texture spaceshipTexture;
    private final Image spaceship;
    private final Texture pauseTexture;
    private float backgroundY1;
    private float backgroundY2;
    private final ArrayList<Bullets> bullets;
    private final ArrayList<Asteroids> asteroids;
    private final ArrayList<BonusFood> bonusFoods;
    private final Random random;
    private float shootTimer;
    private float asteroidsSpawnTimer;
    private final Music backgroundMusic;

    private boolean paused;
    private boolean gameOver;

    private static final float SPEED = 200;
    private static final float SHOOT_WAIT_TIME = 0.3f;
    private static final float MIN_ASTEROIDS_SPAWN_TIME = 0.3f;
    private static final float MAX_ASTEROIDS_SPAWN_TIME = 0.6f;

    private final com.mygdx.game.mechanism.GameInfo gameInfo;

    private final Rectangle spaceshipBounds;

    private int asteroidKillCount;
    private final float splitRatio = 0.80f;

    private int life;
    private final int selectedShip;

    public GameScreen(MyGame game, int selectedShip) {
        this.game = game;
        this.stage = new Stage();
        this.selectedShip = selectedShip;

        this.backgroundTexture1 = new Texture(Gdx.files.internal("background3.jpg"));
        this.backgroundTexture2 = new Texture(Gdx.files.internal("background3.jpg"));

        String[] shipTextures = {
                "blue_ship.png",
                "red_ship.png",
                "green_ship.png",
                "orange_ship.png"
        };
        this.spaceshipTexture = new Texture(Gdx.files.internal(shipTextures[selectedShip]));
        this.spaceship = new Image(spaceshipTexture);
        spaceship.setPosition(200, 10);
        stage.addActor(spaceship);

        backgroundY1 = 0;
        backgroundY2 = backgroundTexture1.getHeight();

        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        bonusFoods = new ArrayList<>();
        random = new Random();
        shootTimer = 0;
        asteroidsSpawnTimer = random.nextFloat() * (MAX_ASTEROIDS_SPAWN_TIME - MIN_ASTEROIDS_SPAWN_TIME) + MIN_ASTEROIDS_SPAWN_TIME;

        paused = false;
        gameOver = false;

        gameInfo = new com.mygdx.game.mechanism.GameInfo();
        asteroidKillCount = 0;

        life = 3;

        this.pauseTexture = new Texture(Gdx.files.internal("pauseButton.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseTexture);
        Image pauseButton = new Image(pauseDrawable);
        float buttonSize = Gdx.graphics.getWidth() * 0.07f;
        pauseButton.setSize(buttonSize, buttonSize);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 40, Gdx.graphics.getHeight() - 550 - pauseButton.getHeight() - 30);
        stage.addActor(pauseButton);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        spaceshipBounds = new Rectangle(spaceship.getX(), spaceship.getY(), spaceship.getWidth(), spaceship.getHeight());

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("faded.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!paused && !gameOver) {
            updateGame(delta);
        }

        float gameAreaWidth = Gdx.graphics.getWidth() * splitRatio;

        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture1, 0, backgroundY1, gameAreaWidth, Gdx.graphics.getHeight());
        stage.getBatch().draw(backgroundTexture2, 0, backgroundY2, gameAreaWidth, Gdx.graphics.getHeight());
        for (Bullets bullet : bullets) {
            bullet.render(stage.getBatch());
        }
        for (Asteroids asteroid : asteroids) {
            asteroid.render(stage.getBatch());
        }
        for (BonusFood bonusFood : bonusFoods) {
            bonusFood.render((SpriteBatch) stage.getBatch());
        }
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

        gameInfo.render();

        if (gameOver) {
            game.setScreen(new GameOverScreen(game, gameInfo.getScore()));
        }
    }

    private void updateGame(float delta) {
        backgroundY1 -= 200 * delta;
        backgroundY2 -= 200 * delta;

        if (backgroundY1 <= -backgroundTexture1.getHeight()) {
            backgroundY1 = backgroundTexture2.getHeight();
        }
        if (backgroundY2 <= -backgroundTexture2.getHeight()) {
            backgroundY2 = backgroundTexture1.getHeight();
        }

        handleInput(delta);

        asteroidsSpawnTimer -= delta;
        if (asteroidsSpawnTimer <= 0) {
            asteroidsSpawnTimer = random.nextFloat() * (MAX_ASTEROIDS_SPAWN_TIME - MIN_ASTEROIDS_SPAWN_TIME) + MIN_ASTEROIDS_SPAWN_TIME;
            asteroids.add(new Asteroids(random.nextInt((int) (Gdx.graphics.getWidth() * splitRatio - Asteroids.WIDTH)), Gdx.graphics.getHeight()));
        }

        ArrayList<Asteroids> asteroidsToRemove = new ArrayList<>();
        ArrayList<Bullets> bulletsToRemove = new ArrayList<>();
        ArrayList<BonusFood> bonusFoodsToRemove = new ArrayList<>();

        for (Bullets bullet : bullets) {
            for (Asteroids asteroid : asteroids) {
                if (bullet.getCollisionRect().overlaps(asteroid.getCollisionRect())) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    gameInfo.increaseScore();
                    asteroidKillCount++;

                    if (asteroidKillCount == 10) {
                        bonusFoods.add(new BonusFood(random.nextInt((int) (Gdx.graphics.getWidth() * splitRatio - BonusFood.WIDTH)), Gdx.graphics.getHeight()));
                        asteroidKillCount = 0;
                    }
                }
            }

            for (BonusFood bonusFood : bonusFoods) {
                if (bullet.getCollisionRect().overlaps(bonusFood.getCollisionRect())) {
                    bulletsToRemove.add(bullet);
                    bonusFoodsToRemove.add(bonusFood);
                    gameInfo.increaseScore(5);
                }
            }
        }

        for (Asteroids asteroid : asteroids) {
            if (spaceshipBounds.overlaps(asteroid.getCollisionRect())) {
                life -= 1;
                gameInfo.decreaseLives();
                asteroidsToRemove.add(asteroid);

                if (life == 0) {
                    gameOver = true;
                }
                break;
            }
        }

        for (Asteroids asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove) {
                asteroidsToRemove.add(asteroid);
            }
        }

        for (Bullets bullet : bullets) {
            bullet.update(delta);
            if (bullet.remove) {
                bulletsToRemove.add(bullet);
            }
        }

        for (BonusFood bonusFood : bonusFoods) {
            bonusFood.update(delta);
            if (bonusFood.remove) {
                bonusFoodsToRemove.add(bonusFood);
            }
        }

        asteroids.removeAll(asteroidsToRemove);
        bullets.removeAll(bulletsToRemove);
        bonusFoods.removeAll(bonusFoodsToRemove);
    }

    private void handleInput(float delta) {
        float x = spaceship.getX();
        float y = spaceship.getY();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= SPEED * delta;
        }

        if (x < 0) x = 0;
        if (x > Gdx.graphics.getWidth() * splitRatio - spaceship.getWidth())
            x = Gdx.graphics.getWidth() * splitRatio - spaceship.getWidth();
        if (y < 0) y = 0;
        if (y > Gdx.graphics.getHeight() - spaceship.getHeight())
            y = Gdx.graphics.getHeight() - spaceship.getHeight();

        spaceship.setPosition(x, y);
        spaceshipBounds.setPosition(x, y);

        shootTimer -= delta;
        if (shootTimer <= 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shootTimer = SHOOT_WAIT_TIME;
            bullets.add(new Bullets(spaceship.getX() + spaceship.getWidth() / 2 - (float) Bullets.WIDTH / 2, spaceship.getY() + spaceship.getHeight()));
        }
    }

    private void togglePause() {
        paused = !paused;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        backgroundMusic.pause();
        dispose();
    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        backgroundTexture1.dispose();
        backgroundTexture2.dispose();
        spaceshipTexture.dispose();
        pauseTexture.dispose();
        stage.dispose();
    }
}
