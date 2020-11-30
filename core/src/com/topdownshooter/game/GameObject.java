package com.topdownshooter.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import datastructures.Direction;
import interfaces.Entity;
import java.util.ArrayList;
import java.util.UUID;


public abstract class GameObject implements Entity {

    protected Vector2 position;
    protected Sprite sprite;

    protected Map map;
    private final String id;

    protected float facing = Direction.right;
    protected ArrayList<Entity> entities;

    public GameObject(Vector2 position, Sprite sprite, Map map, ArrayList<Entity> entities) {
        this.sprite = sprite;
        this.map = map;
        this.position = position;
        this.entities = entities;

        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return sprite.getBoundingRectangle();
    }

    protected void constrainToMap() {
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

    protected void handleCollisions(Vector2 velocity) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.tiledMap.getLayers()
            .get("collision-layer");

        for (Entity e : entities) {
            if (!e.getId().equals(id)) {
                if (e.getBoundingRectangle().overlaps(
                    new Rectangle(getBoundingRectangle().x + velocity.x, getBoundingRectangle().y,
                        getBoundingRectangle().width, getBoundingRectangle().height))) {
                    velocity.x = 0;
                }
                if (e.getBoundingRectangle().overlaps(
                    new Rectangle(getBoundingRectangle().x, getBoundingRectangle().y + velocity.y,
                        getBoundingRectangle().width, getBoundingRectangle().height))) {
                    velocity.y = 0;
                }
            }
        }

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


}
