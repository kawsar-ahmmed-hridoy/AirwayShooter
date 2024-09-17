package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BonusFood {
    private static final float SPEED = 150;
    public static final float WIDTH = 50;
    public static final float HEIGHT = 50;
    private final Texture texture;
    private final float x;
    private float y;
    public boolean remove = false;
    private final Rectangle collisionRect;

    public BonusFood(float x, float y) {
        this.texture = new Texture("bonus.png");
        this.x = x;
        this.y = y;
        this.collisionRect = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        y -= SPEED * delta;
        if (y < -HEIGHT) {
            remove = true;
        }
        collisionRect.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
