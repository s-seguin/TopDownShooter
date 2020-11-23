package com.topdownshooter.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import interfaces.GameObject;

public class Enemy implements GameObject {
    Vector2 position;
    Sprite sprite;

    private final Map map;


    public Enemy(Sprite sprite, Map map) {
        this.sprite = sprite;
        sprite.setOriginCenter();

        this.map = map;

        this.position = new Vector2( 100,  100);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }
}
