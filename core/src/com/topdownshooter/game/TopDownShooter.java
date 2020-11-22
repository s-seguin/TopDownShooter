package com.topdownshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class TopDownShooter extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Sprite hitman1;

    Player player;
    Map map;


    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 512);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas("sprites.txt");
        hitman1 = textureAtlas.createSprite("Hitman 1/hitman1_gun");

        map = new Map(new TmxMapLoader().load("world.tmx"));
        player = new Player(hitman1, map);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        player.update(deltaTime);
        updateCamera();

        map.render(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
        map.dispose();
    }

    private void updateCamera() {
        camera.position.x = player.position.x;
        camera.position.y = player.position.y;
        constrainCameraToMap();
        camera.update();
    }

    private void constrainCameraToMap() {
        if (camera.position.x < camera.viewportWidth / 2) {
            camera.position.x = camera.viewportWidth / 2;
        } else if (camera.position.x > map.width - camera.viewportWidth / 2) {
            camera.position.x = map.width - camera.viewportWidth / 2;
        }

        if (camera.position.y < camera.viewportHeight / 2) {
            camera.position.y = camera.viewportHeight / 2;
        } else if (camera.position.y > map.height - camera.viewportHeight / 2) {
            camera.position.y = map.height - camera.viewportHeight / 2;
        }

    }
}

