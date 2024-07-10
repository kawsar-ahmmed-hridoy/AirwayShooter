package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Random;

public class HomeScreen {
    private final Texture startupBackgroundTexture;
    private final Texture asteroidTexture;
    private final Texture fireTexture;
    public Random random;
    private long startTime;
    private int count;
    private boolean menu;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private static final int AstNum = 20;
    private static final int AstWidth = 20;
    private static final int AstHeight = 20;
    private final Asteroid[] asteroids;
    private final ArrayList<FireEffect> fireEffects;
    private final Music startupMusic;
    private final MenuScreen menuScreen;

    public HomeScreen() {
        Stage stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        random = new Random();

        startupBackgroundTexture = new Texture(Gdx.files.internal("background1.png"));
        asteroidTexture = new Texture(Gdx.files.internal("asteroid.png"));
        fireTexture = new Texture(Gdx.files.internal("fire.png"));

        font = new BitmapFont();
        startTime = TimeUtils.millis();
        count = 1;
        menu = false;

        // Loading && playing startup music.
        startupMusic = Gdx.audio.newMusic(Gdx.files.internal("startup.mp3"));
        startupMusic.setLooping(false);
        startupMusic.play();

        // Creating asteroids
        asteroids = new Asteroid[AstNum];
        for (int i = 0; i < AstNum; i++) {
            asteroids[i] = new Asteroid();
        }

        // Creating fire effects
        fireEffects = new ArrayList<>();

        // Creating Home Menu
        menuScreen = new MenuScreen(stage);
    }

    public void render(float delta) {
        if (!menu) {
            renderStartup(delta);
        } else {
            menuScreen.render(delta);
        }
    }

    private void renderStartup(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(startupBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Updating and drawing asteroids
        for (int i = 0; i < AstNum; i++) {
            asteroids[i].update(delta, asteroids, fireEffects);
            batch.draw(asteroidTexture, asteroids[i].x, asteroids[i].y, AstWidth, AstHeight);
        }

        // Updating and drawing fire effects
        for (FireEffect ff : fireEffects) {
            ff.update(delta);
            batch.draw(fireTexture, ff.x, ff.y);
        }

        // Drawing loading text with count
        String loadingText = "Loading... " + count;
        float textWidth = font.getRegion().getRegionWidth() * font.getData().scaleX;
        float textHeight = font.getRegion().getRegionHeight() * font.getData().scaleY;
        float textX = (Gdx.graphics.getWidth() - textWidth + 100) / 2;
        float textY = (Gdx.graphics.getHeight() + textHeight - 650) / 2;
        font.draw(batch, loadingText, textX, textY);
        batch.end();

        // Removing fire effects
        fireEffects.removeIf(fireEffect -> fireEffect.duration <= 0);

        // Updating count every 100 milliseconds
        if (TimeUtils.timeSinceMillis(startTime) > 10) {
            startTime = TimeUtils.millis();
            count++;
        }
        if (count > 100 && !menu) {
            menu = true;
            startupMusic.stop();
        }
    }

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

            // Check for collisions with screen boundaries
            if (x < 0 || x + AstWidth > Gdx.graphics.getWidth()) {
                velocityX = -velocityX;
                x = MathUtils.clamp(x, 0, Gdx.graphics.getWidth() - AstWidth);
            }
            if (y < 0 || y + AstHeight > Gdx.graphics.getHeight()) {
                velocityY = -velocityY;
                y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() - AstHeight);
            }

            // Checking collisions with other asteroids
            for (Asteroid other : asteroids) {
                if (other != this && bounds.overlaps(other.bounds)) {
                    // Reverse velocities
                    velocityX = -velocityX;
                    velocityY = -velocityY;
                    other.velocityX = -other.velocityX;
                    other.velocityY = -other.velocityY;

                    // Ensuring the asteroids move apart
                    x += velocityX * delta;
                    y += velocityY * delta;
                    other.x += other.velocityX * delta;
                    other.y += other.velocityY * delta;

                    // Creating fire effect at the collision spot
                    fireEffects.add(new FireEffect((x + other.x) / 2, (y + other.y) / 2));

                    // Updating bounds positions
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
            this.duration = 1.0f; // Fire effect duration in seconds
        }
        void update(float delta) {
            duration -= delta;
        }
    }
}
