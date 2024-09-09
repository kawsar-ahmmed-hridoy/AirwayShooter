package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Asteroids {
    private static final float SPEED = 200;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public boolean remove = false;

    private final float x;
    private float y;
    private final Texture texture;
    private final Rectangle collisionRect;

    private static final String[] ASTEROID_TEXTURES = {
            "green.png",
            "blue.png",
            "purple.png",
            "yellow.png",
            "violet.png"
    };

    private static final Random random = new Random();

    public Asteroids(float x, float startY) {
        this.x = x;
        this.y = startY;
        int randomIndex = random.nextInt(ASTEROID_TEXTURES.length);
        this.texture = new Texture(ASTEROID_TEXTURES[randomIndex]);
        this.collisionRect = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        y -= SPEED * delta;
        if (y < -HEIGHT) {
            remove = true;
        }
        collisionRect.setPosition(x, y);
    }

    public void render(Batch batch) {
        batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
