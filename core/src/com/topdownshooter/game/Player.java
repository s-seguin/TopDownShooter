package com.topdownshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player {
    Vector2 position = new Vector2(0, 0);
    Sprite sprite;

    private float speed = 10f;
    private float acceleration = 90;
    private float maxSpeed = 10;

    public Player(Sprite sprite) {
        this.sprite = sprite;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += calculateDistanceMoved(deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= calculateDistanceMoved(deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += calculateDistanceMoved(deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= calculateDistanceMoved(deltaTime);
        }
    }

    private float calculateDistanceMoved(float deltaTime) {
        return MathUtils.clamp(speed * acceleration * deltaTime, 0, maxSpeed);
    }
}
