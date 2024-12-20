package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGame;

import java.util.ArrayList;
import java.util.Random;

public class HomeScreen implements Screen {
    private final Stage stage;
    private final MyGame game;
    private final Texture startupBackgroundTexture;
    private final Texture asteroidTexture;
    private final Texture fireTexture;
    private final Random random;
    private long startTime;
    private int count;
    private boolean menuActive;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private static final int AstNum = 20;
    private static final int AstWidth = 25;
    private static final int AstHeight = 25;
    private final Asteroid[] asteroids;
    private final ArrayList<FireEffect> fireEffects;
    private final Music startupMusic;
    private final MenuScreen menuScreen;

    public HomeScreen(MyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        random = new Random();

        startupBackgroundTexture = new Texture(Gdx.files.internal("background1.png"));
        asteroidTexture = new Texture(Gdx.files.internal("homeAstro.png"));
        fireTexture = new Texture(Gdx.files.internal("fire.png"));

        font = new BitmapFont();
        startTime = System.currentTimeMillis();
        count = 1;
        menuActive = false;

        startupMusic = Gdx.audio.newMusic(Gdx.files.internal("startup.mp3"));
        startupMusic.setLooping(false);
        startupMusic.play();

        asteroids = new Asteroid[AstNum];
        for (int i = 0; i < AstNum; i++) {
            asteroids[i] = new Asteroid();
        }

        fireEffects = new ArrayList<>();

        menuScreen = new MenuScreen(game, stage);

        Image transitionImage = new Image(new Texture(Gdx.files.internal("fire.png")));
        transitionImage.setPosition(100, 100);
        transitionImage.setSize(100, 50);
        transitionImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuActive = true;
                startupMusic.stop();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!menuActive) {
            renderStartup(delta);
        } else {
            menuScreen.render(delta);
        }
    }

    private void renderStartup(float delta) {
        batch.begin();
        batch.draw(startupBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < AstNum; i++) {
            asteroids[i].update(delta, asteroids, fireEffects);
            batch.draw(asteroidTexture, asteroids[i].x, asteroids[i].y, AstWidth, AstHeight);
        }

        for (FireEffect ff : fireEffects) {
            ff.update(delta);
            batch.draw(fireTexture, ff.x, ff.y);
        }

        String loadingText = "Loading... " + count;
        float textWidth = font.getRegion().getRegionWidth() * font.getData().scaleX;
        float textHeight = font.getRegion().getRegionHeight() * font.getData().scaleY;
        float textX = (Gdx.graphics.getWidth() - textWidth + 100) / 2;
        float textY = (Gdx.graphics.getHeight()-250 + textHeight - 350) / 2;
        font.draw(batch, loadingText, textX, textY);
        batch.end();

        fireEffects.removeIf(fireEffect -> fireEffect.duration <= 0);

        if (System.currentTimeMillis() - startTime > 100) {
            startTime = System.currentTimeMillis();
            count++;
        }

        if (count > 100 && !menuActive) {
            menuActive = true;
            startupMusic.stop();
            game.setScreen(menuScreen);
        }
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        startupBackgroundTexture.dispose();
        asteroidTexture.dispose();
        fireTexture.dispose();
        startupMusic.dispose();
        menuScreen.dispose();
    }

    private class Asteroid {
        float x, y;
        float velocityX, velocityY;
        Rectangle bounds;

        Asteroid() {
            x = random.nextFloat() * Gdx.graphics.getWidth();
            y = random.nextFloat() * Gdx.graphics.getHeight();
            velocityX = (random.nextFloat() - 0.5f) * 200;
            velocityY = (random.nextFloat() - 0.5f) * 200;
            bounds = new Rectangle(x, y, AstWidth, AstHeight);
        }

        void update(float delta, Asteroid[] asteroids, ArrayList<FireEffect> fireEffects) {
            x += velocityX * delta;
            y += velocityY * delta;
            bounds.setPosition(x, y);

            if (x < 0 || x + AstWidth > Gdx.graphics.getWidth()) {
                velocityX = -velocityX;
                x = MathUtils.clamp(x, 0, Gdx.graphics.getWidth() - AstWidth);
            }
            if (y < 0 || y + AstHeight > Gdx.graphics.getHeight()) {
                velocityY = -velocityY;
                y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() - AstHeight);
            }

            for (Asteroid other : asteroids) {
                if (other != this && bounds.overlaps(other.bounds)) {
                    velocityX = -velocityX;
                    velocityY = -velocityY;
                    other.velocityX = -other.velocityX;
                    other.velocityY = -other.velocityY;

                    x += velocityX * delta;
                    y += velocityY * delta;
                    other.x += other.velocityX * delta;
                    other.y += other.velocityY * delta;

                    fireEffects.add(new FireEffect((x + other.x) / 2, (y + other.y) / 2));

                    bounds.setPosition(x, y);
                    other.bounds.setPosition(other.x, other.y);
                }
            }
        }
    }

    private static class FireEffect {
        float x, y, duration;

        FireEffect(float x, float y) {
            this.x = x;
            this.y = y;
            this.duration = 1.0f;
        }

        void update(float delta) {
            duration -= delta;
        }
    }
}
