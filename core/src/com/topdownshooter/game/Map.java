package com.topdownshooter.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;

public class Map {
    TiledMap tiledMap;
    OrthoCachedTiledMapRenderer tiledMapRenderer;
    int width;
    int height;
    MapProperties properties;

    public Map(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
        tiledMapRenderer.setBlending(true);

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
