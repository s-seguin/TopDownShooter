package com.topdownshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import datastructures.Direction;
import interfaces.Entity;
import java.util.ArrayList;

public class Player extends GameObject {

    public Player(Sprite sprite, Map map, ArrayList<Entity> entities) {
        super(new Vector2(map.width / 2f, map.height / 2f), sprite, map, entities);
        sprite.setOriginCenter();
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.setRotation(facing);
        sprite.draw(batch);
    }

    public void update(float deltaTime) {
        Vector2 velocity = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y = calculateDistanceMoved(deltaTime);
            facing = Direction.up;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y = -calculateDistanceMoved(deltaTime);
            facing = Direction.down;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x += calculateDistanceMoved(deltaTime);
            facing = Direction.right;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -calculateDistanceMoved(deltaTime);
            facing = Direction.left;
        }


        handleCollisions(velocity);
        position.x += velocity.x;
        position.y += velocity.y;

        constrainToMap();
    }

    private float calculateDistanceMoved(float deltaTime) {
        float speed = 10f;
        float acceleration = 90;
        float maxSpeed = 10;
        return MathUtils.clamp(speed * acceleration * deltaTime, 0, maxSpeed);
    }


}
