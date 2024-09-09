package com.mygdx.game.mechanism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameInfo {
    private int score;
    private int lives;
    private final int bestScore;
    private final BitmapFont font;
    private final SpriteBatch batch;

    FileHandle file = Gdx.files.local("bestScores.txt");
    public GameInfo() {
        score = 0;
        lives = 3;
        bestScore = Integer.parseInt(file.readString().trim());
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    public void increaseScore() {
        score++;
    }
    public void increaseScore(int value) {
        score += value;
    }

    public void decreaseLives() {
        lives--;
    }

    public int getScore() {
        return score;
    }

    public void render() {
        batch.begin();
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Lives: " + lives, Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Best Score: " + bestScore, Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() - 80);
        batch.end();
    }
}
