package com.topdownshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import datastructures.Direction;
import interfaces.GameObject;

public class Player implements GameObject {
    Vector2 position;
    Sprite sprite;

    private final Map map;

    private float facing = Direction.right;

    public Player(Sprite sprite, Map map) {
        this.sprite = sprite;
        sprite.setOriginCenter();

        this.map = map;

        this.position = new Vector2( map.width / 2f,  map.height / 2f);
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

    private void handleCollisions(Vector2 velocity) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.tiledMap.getLayers().get("collision-layer");

        velocity.x = (velocity.x > 0) ?
                handleCollisionRight(collisionLayer, velocity.x) :
                handleCollisionLeft(collisionLayer, velocity.x);
        velocity.y = (velocity.y > 0) ?
                handleCollisionUp(collisionLayer, velocity.y) :
                handleCollisionDown(collisionLayer, velocity.y);
    }

    private float handleCollisionRight(TiledMapTileLayer collisionLayer, float xVelocity) {
        float rightEdge = sprite.getBoundingRectangle().x + sprite.getBoundingRectangle().width;

        int col = (int) Math.ceil(position.x / collisionLayer.getTileWidth());
        int row = Math.round(position.y / collisionLayer.getTileHeight());

        return handleCollision(col, row, collisionLayer, (col * collisionLayer.getTileWidth() - rightEdge), xVelocity);
    }

    private float handleCollisionLeft(TiledMapTileLayer collisionLayer, float xVelocity) {
        int col = (int) Math.floor(position.x / collisionLayer.getTileWidth());
        int row = Math.round(position.y / collisionLayer.getTileHeight());

        return handleCollision(col, row, collisionLayer, 0, xVelocity);
    }

    private float handleCollisionUp(TiledMapTileLayer collisionLayer, float yVelocity) {
        float topEdge = sprite.getBoundingRectangle().y + sprite.getBoundingRectangle().height;

        int col = Math.round(position.x / collisionLayer.getTileWidth());
        int row = (int) Math.ceil(position.y / collisionLayer.getTileHeight());

        return handleCollision(col, row, collisionLayer, (row * collisionLayer.getTileHeight() - topEdge), yVelocity);

    }

    private float handleCollisionDown(TiledMapTileLayer collisionLayer, float yVelocity) {
        int col = Math.round(position.x / collisionLayer.getTileWidth());
        int row = (int) Math.floor(position.y / collisionLayer.getTileHeight());

        return handleCollision(col, row, collisionLayer, 0, yVelocity);
    }

    private float handleCollision(int col, int row, TiledMapTileLayer collisionLayer, float updatedVelocity, float originalVelocity) {
        return collisionLayer.getCell(col, row) != null ? updatedVelocity : originalVelocity;
    }

    private void constrainToMap() {
        if (position.x < 0) {
            position.x = 0;
        } else if (position.x > map.width - sprite.getBoundingRectangle().width) {
            position.x = map.width - sprite.getBoundingRectangle().width;
        }

        if (position.y < 0) {
            position.y = 0;
        } else if (position.y > map.height - sprite.getBoundingRectangle().height) {
            position.y = map.height - sprite.getBoundingRectangle().height;
        }
    }
}
