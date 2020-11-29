package com.topdownshooter.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends GameObject {

    public Enemy(Sprite sprite, Map map) {
        super(new Vector2( 100,  100), sprite, map);
    }

    @Override
    public void update(float deltaTime) {
        float x = (float) ((Math.random() > 0.5 ? 1 : -1) * Math.random()*10);
        float y = (float) ((Math.random() > 0.5 ? 1 : -1) * Math.random()*10);
        Vector2 velocity = new Vector2(x,y);

        handleCollisions(velocity);

        position.x += velocity.x;
        position.y += velocity.y;
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
