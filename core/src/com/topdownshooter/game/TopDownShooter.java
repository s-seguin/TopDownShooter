package com.topdownshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TopDownShooter extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Sprite hitman1;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 640, 512);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas("sprites.txt");
        hitman1 = textureAtlas.createSprite("Hitman 1/hitman1_gun");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        paintBackground();
        batch.draw(hitman1, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
    }

    // paint a grass tile for background that is in view of camera
    // todo look into tiled maps:
    // https://gamefromscratch.com/libgdx-tutorial-11-tiled-maps-part-1-simple-orthogonal-maps/
    // https://gamefromscratch.com/libgdx-tutorial-11-tiled-maps-part-2-adding-a-sprite-and-dealing-with-layers/
    private void paintBackground() {
        Sprite grassTile = textureAtlas.createSprite("Tiles/tile", 1);
        for (int x = 0; x <= camera.viewportWidth; x += 64) {
            for (int y = 0; y <=camera.viewportHeight; y += 6) {
                batch.draw(grassTile, x, y);
            }
        }
    }
}

