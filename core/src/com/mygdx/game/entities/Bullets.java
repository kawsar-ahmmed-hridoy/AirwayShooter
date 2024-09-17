package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Bullets {
    private static final float SPEED = 500;
    public static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    public boolean remove = false;

    private final float x;
    private float y;
    private final Texture texture;
    private final Rectangle collisionRect;

    public Bullets(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = new Texture("laser.png");
        this.collisionRect = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        y += SPEED * delta;
        if (y > com.badlogic.gdx.Gdx.graphics.getHeight()) {
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
