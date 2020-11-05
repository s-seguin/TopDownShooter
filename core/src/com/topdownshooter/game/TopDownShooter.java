package com.topdownshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

class Map {
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    int width;
    int height;
    MapProperties properties;

    public Map(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);

        properties = tiledMap.getProperties();

        width = properties.get("width", Integer.class) * properties.get("tilewidth", Integer.class);
        height = properties.get("height", Integer.class) * properties.get("tileheight", Integer.class);
    }

    public void render(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void dispose() {
        tiledMap.dispose();
    }

}

class Player {
    Vector2 position = new Vector2(0, 0);
    float speed = 10f;

    Sprite sprite;

    public Player(Sprite sprite) {
        this.sprite = sprite;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y -= speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y += speed;
        }

    }
}

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
        camera.setToOrtho(true, 640, 512);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas("sprites.txt");
        hitman1 = textureAtlas.createSprite("Hitman 1/hitman1_gun");

        player = new Player(hitman1);
        map = new Map(new TmxMapLoader().load("world.tmx"));

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime);
        constrainPlayerToMap();

        camera.position.x = player.position.x;
        camera.position.y = player.position.y;
        constrainCameraToMap();
        camera.update();

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

    private void constrainPlayerToMap() {
        if (player.position.x < 0) {
            player.position.x = 0;
        } else if (player.position.x > map.width - player.sprite.getBoundingRectangle().width) {
            player.position.x = map.width - player.sprite.getBoundingRectangle().width;
        }

        if (player.position.y < 0) {
            player.position.y = 0;
        } else if (player.position.y > map.height - player.sprite.getBoundingRectangle().height) {
            player.position.y = map.height - player.sprite.getBoundingRectangle().height;
        }
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

