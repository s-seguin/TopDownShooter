package com.topdownshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Direction {
    public static final float up = 90;
    public static final float down = 270;
    public static final float left = 180;
    public static final float right = 0;
}

public class Player {
    Vector2 position = new Vector2(0, 0);
    Sprite sprite;

    private Map map;

    private float speed = 10f;
    private float acceleration = 90;
    private float maxSpeed = 10;

    private float facing = Direction.right;

    public Player(Sprite sprite, Map map) {
        this.sprite = sprite;
        sprite.setOriginCenter();

        this.map = map;
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
//        boolean isColliding = isColliding();
//        System.out.println("isColling " + isColliding);
//        if (isColliding) {
//            System.out.println("here");
//            position = prevPosition;
//        }

        constrainToMap();
    }

    private float calculateDistanceMoved(float deltaTime) {
        return MathUtils.clamp(speed * acceleration * deltaTime, 0, maxSpeed);
    }

    private void handleCollisions(Vector2 velocity) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.tiledMap.getLayers().get("house");

        float topEdge = sprite.getBoundingRectangle().y + sprite.getBoundingRectangle().height;
        float bottomEdge = sprite.getBoundingRectangle().y;
        float rightEdge = sprite.getBoundingRectangle().x + sprite.getBoundingRectangle().width;
        float leftEdge = sprite.getBoundingRectangle().x;

//        printMap(collisionLayer);

        // x collisions

        //which row of tiles intersect with bottom edge and topEdge
        int topRow = (int) Math.floor(topEdge / collisionLayer.getTileHeight());
        int bottomRow = (int) Math.ceil(bottomEdge / collisionLayer.getTileHeight());

        if (velocity.x > 0) {
            //moving right
            System.out.println("moving right");

            int startingCol = (int) Math.floor(rightEdge / collisionLayer.getTileWidth());

            // find closest impassable object
            // for intersecting rows
            for (int row = bottomRow - 1; row < topRow; row++) {
                for (int col = startingCol; col < collisionLayer.getWidth(); col++) {
                    TiledMapTileLayer.Cell cell = collisionLayer.getCell(col, row);

                    if (cell != null) {
                        float distanceToClosestObj = (col * collisionLayer.getTileWidth() - rightEdge);
                        velocity.x = Math.min(velocity.x, distanceToClosestObj);
                        return;
                    }

                }
            }

        } else if (velocity.x < 0) {
            System.out.println("moving left");

            int startingCol = (int) Math.ceil(leftEdge / collisionLayer.getTileWidth());

            // find closest impassable object
            // for intersecting rows
            for (int row = bottomRow - 1; row < topRow; row++) {
                for (int col = startingCol; col >= 0; col--) {
                    TiledMapTileLayer.Cell cell = collisionLayer.getCell(col, row);

                    if (cell != null) {
                        float distanceToClosestObj = leftEdge - (col * collisionLayer.getTileWidth() + collisionLayer.getTileWidth());
                        velocity.x = -Math.min(Math.abs(velocity.x), Math.abs(distanceToClosestObj));
                        return;
                    }

                }
            }
        }

        // y collisions
        int leftCol = (int) Math.floor(leftEdge / collisionLayer.getTileWidth());
        int rightCol = (int) Math.ceil(rightEdge / collisionLayer.getTileWidth());

        if (velocity.y > 0) {

            System.out.println("moving up");

            int startingRow = (int) Math.floor(topEdge / collisionLayer.getTileHeight());
            for (int col = leftCol - 1; col < rightCol; col++) {
                System.out.printf("Col %d leftCol %d rightCol %d topEdge %f%n", col, leftCol - 1, rightCol, topEdge);
                for (int row = startingRow; row <= collisionLayer.getHeight(); row++) {
                    System.out.printf("Row %d%n", row);
                    TiledMapTileLayer.Cell cell = collisionLayer.getCell(col, row);

                    if (cell != null) {
                        float distanceToClosestObj = (row * collisionLayer.getTileHeight()) - topEdge;
                        velocity.y = Math.min(Math.abs(velocity.y), Math.abs(distanceToClosestObj));
                        return;
                    }
                }
            }
        } else if (velocity.y < 0) {

            System.out.println("moving down");

            int startingRow = (int) Math.floor(bottomEdge / collisionLayer.getTileHeight());
            for (int col = leftCol - 1; col < rightCol; col++) {
                System.out.printf("Col %d leftCol %d rightCol %d topEdge %f%n", col, leftCol - 1, rightCol, topEdge);
                for (int row = startingRow; row >= 0; row--) {
                    System.out.printf("Row %d%n", row);
                    TiledMapTileLayer.Cell cell = collisionLayer.getCell(col, row);

                    if (cell != null) {
                        float distanceToClosestObj = bottomEdge - (row * collisionLayer.getTileHeight() + collisionLayer.getTileHeight());
                        velocity.y = -Math.min(Math.abs(velocity.y), Math.abs(distanceToClosestObj));
                        return;
                    }
                }
            }
        }
    }

    private void printMap(TiledMapTileLayer collisionLayer) {
        float playerPosX = Math.round(position.x / collisionLayer.getTileWidth());
        float playerPosY = Math.round(position.y / collisionLayer.getTileHeight());

        for (int row = 0; row < collisionLayer.getWidth(); row++) {
            for (int col = 0; col < collisionLayer.getHeight(); col++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(col, row);
                char c = '_';
                if (playerPosX == col && playerPosY == row && cell != null) {
                    c = '*';
                } else if (playerPosX == col && playerPosY == row) {
                    c = 'P';
                } else if (cell != null) {
                    c = 'X';
                }
                System.out.print(c + " ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
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
